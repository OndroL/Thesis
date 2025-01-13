--
-- PostgreSQL database dump
--

-- Dumped from database version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: unaccent; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS unaccent WITH SCHEMA public;


--
-- Name: EXTENSION unaccent; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION unaccent IS 'text search dictionary that removes accents';


--
-- Name: add_column(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.add_column(tablename text, columnname text, typename text) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
    BEGIN
	IF NOT EXISTS(SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE LOWER(TABLE_NAME) = LOWER($1) AND LOWER(COLUMN_NAME) = LOWER($2)) THEN
	    EXECUTE format('ALTER TABLE %s ADD COLUMN %s %s', LOWER(tableName), LOWER(columnName), LOWER(typeName));
	    RETURN TRUE;
	ELSE
	    RETURN FALSE;
	END IF;
    END;
$_$;


ALTER FUNCTION public.add_column(tablename text, columnname text, typename text) OWNER TO postgres;

--
-- Name: add_index(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.add_index(indexname text, command text) RETURNS void
    LANGUAGE plpgsql
    AS $$
                        BEGIN
                            BEGIN
                                EXECUTE format('DROP INDEX IF EXISTS %s', LOWER(indexName));
                            EXCEPTION WHEN others THEN
                                RAISE NOTICE 'Nepodarilo se odstranit index %', indexName;
                            END;
                            BEGIN
                                EXECUTE command;
                            EXCEPTION WHEN others THEN
                                RAISE NOTICE 'Nepodarilo se vytvorit index %', indexName;
                            END;
                        END;	
                    $$;


ALTER FUNCTION public.add_index(indexname text, command text) OWNER TO postgres;

--
-- Name: areal_ischild(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.areal_ischild(text, text) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$ DECLARE
parentid ALIAS FOR $1;
childid ALIAS FOR $2;
arealrecord RECORD;
BEGIN
	IF childid = parentid THEN RETURN true; END IF;
	SELECT a.* INTO arealrecord FROM areal a WHERE id = childid;
	IF arealrecord.nadrazeny_areal IS NULL THEN RETURN false; END IF;
	RETURN areal_isChild(parentid, arealrecord.nadrazeny_areal);
END$_$;


ALTER FUNCTION public.areal_ischild(text, text) OWNER TO postgres;

--
-- Name: customer_has_parent(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.customer_has_parent(text, text) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE customerid ALIAS FOR $1;
parentid ALIAS FOR $2;
BEGIN
   RETURN EXISTS (SELECT z.id FROM zakaznik z WHERE z.id=customerid AND z.zakaznikparent = parentid);
END$_$;


ALTER FUNCTION public.customer_has_parent(text, text) OWNER TO postgres;

--
-- Name: have_zbozi_pohyb(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.have_zbozi_pohyb(text, text) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE 
  os RECORD;
BEGIN
  RETURN (SELECT id FROM pohyb_zbozi WHERE zbozi = $1 AND sklad = $2 LIMIT 1) is not null;
END;
$_$;


ALTER FUNCTION public.have_zbozi_pohyb(text, text) OWNER TO postgres;

--
-- Name: is_base_objekt(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.is_base_objekt(text) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
BEGIN
  RETURN (SELECT objekt FROM nadobjekt_objekt WHERE nadobjekt = $1 LIMIT 1) is NULL;
END
$_$;


ALTER FUNCTION public.is_base_objekt(text) OWNER TO postgres;

--
-- Name: kat_ischild(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.kat_ischild(text, text) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$ DECLARE
parentid ALIAS FOR $1;
childid ALIAS FOR $2;
katrecord RECORD;
BEGIN
	IF childid = parentid THEN RETURN true; END IF;
	SELECT k.* INTO katrecord FROM kategorie k WHERE id = childid;
	IF katrecord.nadkategorie IS NULL THEN RETURN false; END IF;
	RETURN kat_isChild(parentid, katrecord.nadkategorie);
END$_$;


ALTER FUNCTION public.kat_ischild(text, text) OWNER TO postgres;

--
-- Name: pg_file_length(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pg_file_length(text) RETURNS bigint
    LANGUAGE sql STRICT
    AS $_$SELECT len FROM pg_file_stat($1) AS s(len int8, c timestamp, a timestamp, m timestamp, i bool)$_$;


ALTER FUNCTION public.pg_file_length(text) OWNER TO postgres;

--
-- Name: pg_file_rename(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pg_file_rename(text, text) RETURNS boolean
    LANGUAGE sql STRICT
    AS $_$SELECT pg_file_rename($1, $2, NULL); $_$;


ALTER FUNCTION public.pg_file_rename(text, text) OWNER TO postgres;

--
-- Name: plpgsql_call_handler(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.plpgsql_call_handler() RETURNS language_handler
    LANGUAGE c
    AS '$libdir/plpgsql', 'plpgsql_call_handler';


ALTER FUNCTION public.plpgsql_call_handler() OWNER TO postgres;

--
-- Name: plpgsql_validator(oid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.plpgsql_validator(oid) RETURNS void
    LANGUAGE c
    AS '$libdir/plpgsql', 'plpgsql_validator';


ALTER FUNCTION public.plpgsql_validator(oid) OWNER TO postgres;

--
-- Name: skupina_zakazniku_sort(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.skupina_zakazniku_sort() RETURNS void
    LANGUAGE plpgsql
    AS $$
  DECLARE
    skupinaZakaznikuId TEXT;
  BEGIN
    CREATE SEQUENCE skupinaZakazniku;
    FOR skupinaZakaznikuId IN select sz.id FROM skupina_zakazniku sz, skupina_zakazniku_loc szl WHERE sz.id = szl.skupina ORDER BY szl.nazev 
      LOOP
        UPDATE skupina_zakazniku SET index = nextval('skupinaZakazniku') WHERE id = skupinaZakaznikuId;
     END LOOP;
    DROP SEQUENCE skupinaZakazniku;
  END;
$$;


ALTER FUNCTION public.skupina_zakazniku_sort() OWNER TO postgres;

--
-- Name: to_ascii(bytea, name); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.to_ascii(bytea, name) RETURNS text
    LANGUAGE internal STRICT
    AS $$to_ascii_encname$$;


ALTER FUNCTION public.to_ascii(bytea, name) OWNER TO postgres;

--
-- Name: zakaznik_skupina(text, timestamp with time zone); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.zakaznik_skupina(text, timestamp with time zone) RETURNS text
    LANGUAGE plpgsql
    AS $_$DECLARE        zakaznikid ALIAS FOR $1;        toTime ALIAS FOR $2;        skupina TEXT;BEGIN        SELECT c.skupinazakazniku INTO skupina FROM clen_skupiny_zakazniku c, skupina_zakazniku sz WHERE                c.zakaznik=zakaznikid AND c.skupinazakazniku = sz.id AND ((c.clenstviod IS NULL) OR (c.clenstviod < toTime)) AND                ((c.clenstvido IS NULL) OR (c.clenstvido > toTime)) ORDER BY sz.index LIMIT 1;        RETURN skupina;END$_$;


ALTER FUNCTION public.zakaznik_skupina(text, timestamp with time zone) OWNER TO postgres;

--
-- Name: zakaznik_skupina(text, text, timestamp with time zone); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.zakaznik_skupina(text, text, timestamp with time zone) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
                        DECLARE 
                        idzakaznika ALIAS FOR $1;
                        skupinaid ALIAS FOR $2;
                        toTime ALIAS FOR $3;
                        ret BOOLEAN;
                        BEGIN
                          IF skupinaid = 'bezSkupinyId' THEN
                            RETURN (SELECT COUNT (*) FROM clen_skupiny_zakazniku c, skupina_zakazniku sz, zakaznik z WHERE z.id=idzakaznika 
                                AND (c.zakaznik=z.id  OR  c.zakaznik=z.zakaznikparent) 
                                AND c.skupinazakazniku = sz.id 
                                AND ((c.clenstviod IS NULL) OR (c.clenstviod < toTime))
                                AND ((c.clenstvido IS NULL) OR (c.clenstvido > toTime)) 
                                AND ((select count(*) from prerusene_clenstvi where (zakaznikid=z.id OR zakaznikid=z.zakaznikparent) and preruseniod <= now() and prerusenido >= now()) = 0))
                            = 0;
                          ELSE
                            SELECT skupinaid IN 
                                (SELECT c.skupinazakazniku FROM clen_skupiny_zakazniku c, skupina_zakazniku sz, zakaznik z WHERE z.id=idzakaznika 
                                AND (c.zakaznik=z.id  OR  c.zakaznik=z.zakaznikparent)
                                AND c.skupinazakazniku = sz.id
                                AND ((c.clenstviod IS NULL) OR (c.clenstviod < toTime))
                                AND ((c.clenstvido IS NULL) OR (c.clenstvido > toTime)) 
                                AND ((select count(*) from prerusene_clenstvi where (zakaznikid=z.id OR zakaznikid=z.zakaznikparent) and preruseniod <= now() and prerusenido >= now()) = 0)) INTO ret; 
                            RETURN ret;
                          END IF;
                        END;
                    $_$;


ALTER FUNCTION public.zakaznik_skupina(text, text, timestamp with time zone) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: activity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activity (
    id text NOT NULL,
    name text,
    description text,
    iconid text,
    index integer
);


ALTER TABLE public.activity OWNER TO postgres;

--
-- Name: activity_favourite; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activity_favourite (
    id text NOT NULL,
    zakaznikid text,
    activityid text,
    datumposlednizmeny timestamp without time zone,
    pocet integer
);


ALTER TABLE public.activity_favourite OWNER TO postgres;

--
-- Name: activity_webtab; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activity_webtab (
    id text NOT NULL,
    sportid text,
    activityid text,
    objectid text,
    tabindex integer NOT NULL
);


ALTER TABLE public.activity_webtab OWNER TO postgres;

--
-- Name: agreement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.agreement (
    id text NOT NULL,
    name text,
    description text,
    validity integer NOT NULL,
    validityunittype integer NOT NULL,
    forms text,
    required boolean DEFAULT false
);


ALTER TABLE public.agreement OWNER TO postgres;

--
-- Name: areal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.areal (
    id text NOT NULL,
    nadrazeny_areal text,
    pocetnavazujucichrez integer
);


ALTER TABLE public.areal OWNER TO postgres;

--
-- Name: areal_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.areal_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    areal text
);


ALTER TABLE public.areal_loc OWNER TO postgres;

--
-- Name: automatic_attachment_template; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.automatic_attachment_template (
    id text NOT NULL,
    templatename text,
    attachmentfilename text,
    templatetype integer NOT NULL,
    datamap bytea,
    automaticmessageid text
);


ALTER TABLE public.automatic_attachment_template OWNER TO postgres;

--
-- Name: automatic_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.automatic_message (
    id text NOT NULL,
    nazev text,
    zakaznici bytea,
    skupiny bytea,
    perioda integer,
    typsablony integer,
    pocatekodesilani timestamp without time zone,
    konecodesilani timestamp without time zone,
    enabled boolean,
    typperiody integer,
    type integer DEFAULT 0,
    fileattachments bytea,
    predmet text,
    telo text
);


ALTER TABLE public.automatic_message OWNER TO postgres;

--
-- Name: automaticke_storno_lekce_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.automaticke_storno_lekce_message (
    id text,
    zacatek timestamp without time zone,
    konec timestamp without time zone,
    objectid text,
    zakaznikid text
);


ALTER TABLE public.automaticke_storno_lekce_message OWNER TO postgres;

--
-- Name: benefity_change; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benefity_change (
    id text NOT NULL,
    login text,
    zadano timestamp without time zone,
    prodlouzeniod timestamp without time zone,
    prodlouzenido timestamp without time zone,
    permanentky boolean NOT NULL,
    clenstvi boolean NOT NULL,
    depozit boolean NOT NULL,
    vouchery boolean NOT NULL
);


ALTER TABLE public.benefity_change OWNER TO postgres;

--
-- Name: blokace_objektu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.blokace_objektu (
    id text NOT NULL,
    typ integer NOT NULL,
    zadavatelid text
);


ALTER TABLE public.blokace_objektu OWNER TO postgres;

--
-- Name: blokace_objektu_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.blokace_objektu_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    blokace text
);


ALTER TABLE public.blokace_objektu_loc OWNER TO postgres;

--
-- Name: cashmatic_transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cashmatic_transaction (
    id text NOT NULL,
    cashmaticid integer,
    createdat timestamp without time zone,
    finishedat timestamp without time zone,
    canceledat timestamp without time zone,
    requestedamount numeric,
    receivedamount numeric,
    dispensedamount numeric,
    notdispensedamount numeric,
    notdispensedamountrefundedby text,
    type text,
    objednavkaid text
);


ALTER TABLE public.cashmatic_transaction OWNER TO postgres;

--
-- Name: casove_omezeni_vv; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.casove_omezeni_vv (
    id text NOT NULL,
    omezeni bytea
);


ALTER TABLE public.casove_omezeni_vv OWNER TO postgres;

--
-- Name: cena; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cena (
    id text NOT NULL,
    platnostod timestamp without time zone,
    skupinaid text,
    cena1 numeric,
    cena2 numeric,
    sazbadph numeric,
    relative boolean NOT NULL,
    zbozi text,
    sazbadphid text
);


ALTER TABLE public.cena OWNER TO postgres;

--
-- Name: cinnost_permanentky_podminka_vstupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cinnost_permanentky_podminka_vstupu (
    sport text NOT NULL,
    podminkavstupu text NOT NULL
);


ALTER TABLE public.cinnost_permanentky_podminka_vstupu OWNER TO postgres;

--
-- Name: cinnost_permanentky_podminka_vystupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cinnost_permanentky_podminka_vystupu (
    podminkavystupu text NOT NULL,
    sport text NOT NULL
);


ALTER TABLE public.cinnost_permanentky_podminka_vystupu OWNER TO postgres;

--
-- Name: clen_skupiny_zakazniku; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clen_skupiny_zakazniku (
    id text NOT NULL,
    clenstviod timestamp without time zone,
    clenstvido timestamp without time zone,
    skupinazakazniku text,
    zakaznik text
);


ALTER TABLE public.clen_skupiny_zakazniku OWNER TO postgres;

--
-- Name: consumption; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.consumption (
    id text NOT NULL,
    platnostdo timestamp without time zone,
    datumvytvoreni timestamp without time zone,
    zadavatelid text,
    jmeno text,
    zakaznikid text
);


ALTER TABLE public.consumption OWNER TO postgres;

--
-- Name: customers_agreement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customers_agreement (
    id text NOT NULL,
    customerid text,
    agreementid text,
    createtime timestamp without time zone,
    revoketime timestamp without time zone,
    expirationtime timestamp without time zone,
    creatorid text,
    revokerid text
);


ALTER TABLE public.customers_agreement OWNER TO postgres;

--
-- Name: delka_vstupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.delka_vstupu (
    id text NOT NULL,
    sportid text,
    minuty integer,
    podminkavstupu text
);


ALTER TABLE public.delka_vstupu OWNER TO postgres;

--
-- Name: deposit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deposit (
    id text NOT NULL,
    mnozstvi numeric,
    zakaznik text,
    typ text,
    platnostdo timestamp without time zone,
    mnozstvibezdph numeric
);


ALTER TABLE public.deposit OWNER TO postgres;

--
-- Name: document; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document (
    id text NOT NULL,
    name text,
    filename text,
    content bytea,
    editor text,
    zakaznikid text
);


ALTER TABLE public.document OWNER TO postgres;

--
-- Name: dodavatel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dodavatel (
    id text NOT NULL,
    nazev text,
    ico text,
    dic text,
    ulice text,
    obec text,
    psc text,
    telefon text,
    mobil text,
    email text,
    web text,
    osoba text,
    banka text,
    ucet text,
    poznamka text
);


ALTER TABLE public.dodavatel OWNER TO postgres;

--
-- Name: eet_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eet_data (
    id text NOT NULL,
    celktrzba numeric,
    cerpzuct numeric,
    dan1 numeric,
    dan2 numeric,
    dan3 numeric,
    dattrzby timestamp with time zone,
    dicpopl text,
    idpokl text,
    idprovoz text,
    poradcis text,
    urcenocerpzuct numeric,
    zakldan1 numeric,
    zakldan2 numeric,
    zakldan3 numeric,
    zaklnepodldph numeric,
    url text,
    overeni boolean,
    priority integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.eet_data OWNER TO postgres;

--
-- Name: eet_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eet_history (
    id text NOT NULL,
    celktrzba numeric,
    cerpzuct numeric,
    dan1 numeric,
    dan2 numeric,
    dan3 numeric,
    dattrzby timestamp without time zone,
    dicpopl text,
    idpokl text,
    idprovoz text,
    poradcis text,
    urcenocerpzuct numeric,
    zakldan1 numeric,
    zakldan2 numeric,
    zakldan3 numeric,
    zaklnepodldph numeric,
    datodeslani timestamp without time zone,
    fik text
);


ALTER TABLE public.eet_history OWNER TO postgres;

--
-- Name: ekasa_device; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ekasa_device (
    id text NOT NULL,
    name text,
    ip text,
    port integer NOT NULL,
    cashdesks text,
    cashregistercode text
);


ALTER TABLE public.ekasa_device OWNER TO postgres;

--
-- Name: email_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email_history (
    id text NOT NULL,
    date timestamp without time zone,
    text text,
    subject text,
    groups bytea,
    recipients bytea,
    morerecipients bytea,
    automatic boolean,
    html boolean,
    attachments bytea,
    sent boolean DEFAULT true
);


ALTER TABLE public.email_history OWNER TO postgres;

--
-- Name: email_queue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email_queue (
    id text NOT NULL,
    created timestamp without time zone,
    emailhistory text,
    recipient text,
    priority integer DEFAULT 0 NOT NULL,
    removeemailhistory boolean DEFAULT false NOT NULL,
    dependentemailhistory text
);


ALTER TABLE public.email_queue OWNER TO postgres;

--
-- Name: eplatba; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eplatba (
    id text NOT NULL,
    cislotransakce text,
    castka numeric NOT NULL,
    currencycode integer NOT NULL,
    datumvzniku timestamp without time zone,
    uspesnedokonceno boolean NOT NULL,
    zakaznikid text,
    objednavkaid text,
    status text,
    typ integer NOT NULL,
    gpstate integer,
    gpstatus text,
    gpsubstatus text,
    objobjektuzaplatitids text,
    objobjektuzaplacene boolean NOT NULL,
    objclenstvizaplacene boolean DEFAULT false,
    objclenstvizaplatitids text,
    objpermzaplacene boolean DEFAULT false,
    objpermzaplatitids text,
    dobijenidepozitu boolean DEFAULT false,
    objzbozizaplacene boolean DEFAULT false,
    objzbozizaplatitid text,
    voucherid text
);


ALTER TABLE public.eplatba OWNER TO postgres;

--
-- Name: expirace_zbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expirace_zbozi (
    id text NOT NULL,
    expirace timestamp without time zone,
    vyprodano boolean NOT NULL,
    jednotek numeric,
    sklad text,
    zbozi text,
    polozka_prijemky text
);


ALTER TABLE public.expirace_zbozi OWNER TO postgres;

--
-- Name: external_client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.external_client (
    id text NOT NULL,
    name text,
    customer_group text,
    oauth2_setting_id text
);


ALTER TABLE public.external_client OWNER TO postgres;

--
-- Name: external_entrance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.external_entrance (
    id text NOT NULL,
    name text,
    type text,
    driverid text,
    address text,
    port integer,
    login text,
    password text,
    otherparams bytea
);


ALTER TABLE public.external_entrance OWNER TO postgres;

--
-- Name: external_entrance_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.external_entrance_item (
    id text NOT NULL,
    zonename text,
    objektid text,
    identification text,
    secondaryidentification text,
    otherparams bytea,
    startinghour integer,
    startingminute integer,
    startingtimerelative boolean,
    endinghour integer,
    endingminute integer,
    endingtimerelative boolean,
    external_entrance text
);


ALTER TABLE public.external_entrance_item OWNER TO postgres;

--
-- Name: external_entrance_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.external_entrance_log (
    id text NOT NULL,
    entranceitemid text,
    objednavkaobjektuid text,
    objednavkaid text,
    tokenid text,
    requestdate timestamp without time zone,
    entrancefrom timestamp without time zone,
    entranceto timestamp without time zone,
    command text,
    finishedwell boolean
);


ALTER TABLE public.external_entrance_log OWNER TO postgres;

--
-- Name: faktura; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.faktura (
    id text NOT NULL,
    cislo text,
    fakturacnisubjektid text,
    datumvystavenifaktury date,
    datumsplatnosti date,
    datumzdanpln date,
    konstantnisymbol text,
    variabilnisymbol text,
    platce text,
    poznamka text,
    externi boolean DEFAULT false
);


ALTER TABLE public.faktura OWNER TO postgres;

--
-- Name: fakturacni_subjekt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.fakturacni_subjekt (
    id text NOT NULL,
    nazevsubjektu text,
    nazevspolecnosti text,
    ico text,
    dic text,
    ulice text,
    obec text,
    psc text,
    telefon text,
    email text,
    web text,
    banka text,
    ucet text,
    iban text,
    swift text,
    sequencename text,
    zapistext text,
    vychozi boolean,
    created timestamp without time zone NOT NULL,
    icdph text,
    viditelny boolean DEFAULT true
);


ALTER TABLE public.fakturacni_subjekt OWNER TO postgres;

--
-- Name: generated_attachment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.generated_attachment (
    id text NOT NULL,
    email text,
    attributes bytea,
    email_history text,
    print_template text
);


ALTER TABLE public.generated_attachment OWNER TO postgres;

--
-- Name: golf_club; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.golf_club (
    cislo text NOT NULL,
    nazev text,
    zkratka text
);


ALTER TABLE public.golf_club OWNER TO postgres;

--
-- Name: golf_hriste; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.golf_hriste (
    id text NOT NULL,
    objekt1id text,
    objekt2id text
);


ALTER TABLE public.golf_hriste OWNER TO postgres;

--
-- Name: golf_odpaliste; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.golf_odpaliste (
    id text NOT NULL,
    cr numeric,
    sl numeric,
    par integer NOT NULL,
    odpaliste_typ text,
    hriste text
);


ALTER TABLE public.golf_odpaliste OWNER TO postgres;

--
-- Name: golf_odpaliste_typ; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.golf_odpaliste_typ (
    id text NOT NULL,
    nazev text,
    barva bytea
);


ALTER TABLE public.golf_odpaliste_typ OWNER TO postgres;

--
-- Name: historie_nastaveni_cen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.historie_nastaveni_cen (
    id text NOT NULL,
    copydate timestamp without time zone,
    platnostdate timestamp without time zone,
    vlozenodate timestamp without time zone,
    kategorie bytea
);


ALTER TABLE public.historie_nastaveni_cen OWNER TO postgres;

--
-- Name: historie_slev; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.historie_slev (
    id text NOT NULL,
    skupina text,
    datum timestamp without time zone,
    baseskupina text,
    basevalue numeric,
    kategorieceny bytea
);


ALTER TABLE public.historie_slev OWNER TO postgres;

--
-- Name: hromadna_objednavka_objektu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hromadna_objednavka_objektu (
    id text NOT NULL,
    zadavatelid text,
    datumvytvoreni timestamp without time zone,
    poznamka text,
    datumstorna timestamp without time zone,
    stornovalid text,
    objektid text,
    zakaznikid text
);


ALTER TABLE public.hromadna_objednavka_objektu OWNER TO postgres;

--
-- Name: html_note; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.html_note (
    id text NOT NULL,
    header text,
    content text,
    zakaznikid text,
    editor text
);


ALTER TABLE public.html_note OWNER TO postgres;

--
-- Name: instructor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.instructor (
    id text NOT NULL,
    firstname text,
    lastname text,
    email text,
    phonenumber text,
    info text,
    color text,
    photo bytea,
    deleted boolean NOT NULL,
    index integer,
    googlecalendarid text,
    googlecalendarnotification boolean DEFAULT false,
    googlecalendarnotificationbefore integer DEFAULT 0,
    phonenumberinternal text,
    emailinternal text,
    phonecode text DEFAULT '+420'::text,
    phonecodeinternal text DEFAULT '+420'::text
);


ALTER TABLE public.instructor OWNER TO postgres;

--
-- Name: instructor_activity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.instructor_activity (
    instructor_id text NOT NULL,
    activity_id text NOT NULL
);


ALTER TABLE public.instructor_activity OWNER TO postgres;

--
-- Name: inventura; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventura (
    id text NOT NULL,
    sklad text,
    datumzadani timestamp without time zone,
    zadal text,
    opravnavydejkaid text,
    opravnaprijemkaid text,
    kategorieveskladu text
);


ALTER TABLE public.inventura OWNER TO postgres;

--
-- Name: jazyk; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jazyk (
    id text NOT NULL,
    nazev text
);


ALTER TABLE public.jazyk OWNER TO postgres;

--
-- Name: jednotka; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jednotka (
    id text NOT NULL
);


ALTER TABLE public.jednotka OWNER TO postgres;

--
-- Name: jednotka_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jednotka_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    zkratka text,
    jednotka text
);


ALTER TABLE public.jednotka_loc OWNER TO postgres;

--
-- Name: kalendar_splatek_clenstvi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kalendar_splatek_clenstvi (
    id text NOT NULL,
    densplatnosti integer NOT NULL,
    typ text,
    datumzacatek timestamp without time zone,
    datumkonec timestamp without time zone,
    datumvytvoreni timestamp without time zone,
    zakaznikid text,
    skupinaid text,
    clenskupinyid text,
    casovemnozstvi numeric,
    casovajednotka integer,
    platebnizboziid text,
    splatnostjednorazovehoclenstviod text
);


ALTER TABLE public.kalendar_splatek_clenstvi OWNER TO postgres;

--
-- Name: kascomp_device; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kascomp_device (
    id text NOT NULL,
    clubspireid integer NOT NULL,
    name text,
    deviceid text,
    ovladani_vstupu text
);


ALTER TABLE public.kascomp_device OWNER TO postgres;

--
-- Name: kategorie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kategorie (
    id text NOT NULL,
    podkategoriecount integer NOT NULL,
    nadkategorie text,
    sklad text,
    fakturacnisubjektid text,
    fiskalizace boolean DEFAULT true,
    viditelna boolean DEFAULT true
);


ALTER TABLE public.kategorie OWNER TO postgres;

--
-- Name: kategorie_fakturacnisubjekt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kategorie_fakturacnisubjekt (
    kategorie text NOT NULL,
    fakturacnisubjekt text NOT NULL
);


ALTER TABLE public.kategorie_fakturacnisubjekt OWNER TO postgres;

--
-- Name: kategorie_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kategorie_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    kategorie text
);


ALTER TABLE public.kategorie_loc OWNER TO postgres;

--
-- Name: license; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.license (
    id text NOT NULL,
    customer text,
    valid boolean,
    centeronline boolean,
    validfrom timestamp without time zone,
    validfromset boolean,
    validto timestamp without time zone,
    validtoset boolean,
    activitylimit integer,
    sportcenterlimit integer,
    sportcustomerslimit integer,
    userslimit integer,
    customergroupslimit integer,
    pokladnalimit integer,
    skladlimit integer,
    maxclients integer,
    ovladaniquido boolean,
    modules bigint,
    hash text,
    createddate timestamp without time zone,
    generateddate timestamp without time zone,
    centerid integer
);


ALTER TABLE public.license OWNER TO postgres;

--
-- Name: member_number; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.member_number (
    id text NOT NULL,
    number text,
    blocked boolean NOT NULL,
    osoba_id text
);


ALTER TABLE public.member_number OWNER TO postgres;

--
-- Name: mena; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mena (
    id text NOT NULL,
    kod text,
    zaokrouhlenihotovost integer NOT NULL,
    zaokrouhlenikarta integer NOT NULL,
    vycetka text,
    kodnum integer NOT NULL
);


ALTER TABLE public.mena OWNER TO postgres;

--
-- Name: message_global_constraint; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_global_constraint (
    id text NOT NULL,
    membershiprepaymentduedatelimit integer,
    membershiprepaymentduedatelimittype integer,
    automatic_message text
);


ALTER TABLE public.message_global_constraint OWNER TO postgres;

--
-- Name: message_omezeni; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_omezeni (
    id text NOT NULL,
    jazyky bytea,
    pohlavi boolean,
    vekod integer,
    vekdo integer,
    hcpod double precision,
    hcpdo double precision,
    automatic_message text,
    agreementids text
);


ALTER TABLE public.message_omezeni OWNER TO postgres;

--
-- Name: nadobjekt_objekt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nadobjekt_objekt (
    nadobjekt text NOT NULL,
    objekt text NOT NULL
);


ALTER TABLE public.nadobjekt_objekt OWNER TO postgres;

--
-- Name: nahradnik; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nahradnik (
    id text NOT NULL,
    objektid text,
    sportid text,
    arealid text,
    zacatek timestamp without time zone,
    konec timestamp without time zone,
    pocetosob integer NOT NULL,
    pocethodakceptace integer,
    datumzadani timestamp without time zone,
    createreservation boolean NOT NULL,
    emailnotification boolean NOT NULL,
    smsnotification boolean NOT NULL,
    zakaznik text,
    pushnotification boolean DEFAULT false NOT NULL,
    vytvoril text
);


ALTER TABLE public.nahradnik OWNER TO postgres;

--
-- Name: nasledne_douctovani; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nasledne_douctovani (
    id text NOT NULL,
    datum timestamp without time zone,
    mnozstvi numeric,
    typ integer NOT NULL,
    polozka_prijemky text,
    sklad text,
    polozka_vydejky text,
    zbozi text
);


ALTER TABLE public.nasledne_douctovani OWNER TO postgres;

--
-- Name: nastaveni; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nastaveni (
    key text NOT NULL,
    value bytea
);


ALTER TABLE public.nastaveni OWNER TO postgres;

--
-- Name: nastaveni_json; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nastaveni_json (
    key text NOT NULL,
    value text
);


ALTER TABLE public.nastaveni_json OWNER TO postgres;

--
-- Name: nastaveni_vstupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nastaveni_vstupu (
    id text NOT NULL,
    nazev text,
    zapojeni integer,
    aktivni boolean,
    ovladani_vstupu text
);


ALTER TABLE public.nastaveni_vstupu OWNER TO postgres;

--
-- Name: noteheader; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.noteheader (
    id text NOT NULL,
    field integer,
    location integer
);


ALTER TABLE public.noteheader OWNER TO postgres;

--
-- Name: oauth2_client_setting; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oauth2_client_setting (
    id text NOT NULL,
    client_id text,
    client_secret text,
    scopes text,
    resource_ids text,
    authorized_grant_types text,
    registered_redirect_uris text,
    auto_approve_scopes text,
    access_token_validity_seconds integer,
    refresh_token_validity_seconds integer
);


ALTER TABLE public.oauth2_client_setting OWNER TO postgres;

--
-- Name: objednavka; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka (
    id text NOT NULL,
    cislo text,
    datumvystaveni timestamp without time zone,
    datumzaplaceni timestamp without time zone,
    datumstorna timestamp without time zone,
    zakaznikid text,
    zadavatelid text,
    stornovalid text,
    typplatby integer NOT NULL,
    platbadepositem boolean NOT NULL,
    cena numeric,
    poznamka text,
    ucetid text,
    mena text,
    cisloplatby text,
    fakepohybdepositu text,
    konzumackaid text,
    typplatebnikartyid text,
    dobaprocedur integer,
    ean text,
    poradi integer DEFAULT 1 NOT NULL,
    oteviracipokladnaid text,
    faktura text,
    terminalpaymentid text,
    cashmatictransactionid text
);


ALTER TABLE public.objednavka OWNER TO postgres;

--
-- Name: objednavka_clenstvi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_clenstvi (
    id text NOT NULL,
    poradi integer NOT NULL,
    cena numeric,
    casovemnozstvi numeric,
    casovajednotka integer NOT NULL,
    zacatekplatnosti timestamp without time zone,
    zakaznikid text,
    skupinaid text,
    platbadepositem boolean NOT NULL,
    objednavka text,
    platebnizboziid text,
    prodlouzeniclenstviid text,
    coef numeric,
    clenskupinyzakaznikuid text,
    splatkaclenstviid text,
    densplatnostisplatek integer,
    typsplatek text,
    discountcode text
);


ALTER TABLE public.objednavka_clenstvi OWNER TO postgres;

--
-- Name: objednavka_depositu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_depositu (
    id text NOT NULL,
    poradi integer NOT NULL,
    cena numeric,
    zakaznikid text,
    typdeposituid text,
    vydejkaid text,
    objednavka text,
    platnost timestamp without time zone,
    pouzenakuphotovosti boolean
);


ALTER TABLE public.objednavka_depositu OWNER TO postgres;

--
-- Name: objednavka_obj_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_obj_token (
    token text NOT NULL,
    objednavka_objektu text NOT NULL
);


ALTER TABLE public.objednavka_obj_token OWNER TO postgres;

--
-- Name: objednavka_objektu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_objektu (
    id text NOT NULL,
    poradi integer NOT NULL,
    typ integer NOT NULL,
    cena numeric,
    sportid text,
    zadavatelid text,
    datumrealizace timestamp without time zone,
    vydejkaid text,
    platbadepositem boolean NOT NULL,
    poznamka text,
    datumstorna timestamp without time zone,
    stornovalid text,
    stornovanobezpoplatku boolean NOT NULL,
    objednavka text,
    datumzaslaniupozorneni timestamp without time zone,
    hromadna_objednavka_objektu text,
    nahradnikemailsent boolean,
    platbapermanentkou boolean DEFAULT false NOT NULL,
    uzavrelvstupid text,
    datumzaslanisms timestamp without time zone,
    objednavkazboziidzaloha text,
    datumvytvoreni timestamp without time zone,
    datumposlednizmeny timestamp without time zone,
    upravilid text,
    nahradniksmssent boolean,
    kod text,
    docasna boolean,
    globalnizaslanisms boolean DEFAULT false NOT NULL,
    globalnizaslaniupozorneni boolean DEFAULT false NOT NULL,
    datumzaslanipush timestamp without time zone,
    globalnizaslanipush boolean DEFAULT false NOT NULL,
    nahradnikpushsent boolean DEFAULT false NOT NULL,
    odstranilid text,
    multisportvisituuid text,
    slevomatvoucherkod text,
    nadrazena_objednavka_objektu text,
    videoodkaz text,
    datumzacatkuvideolekce timestamp without time zone,
    videoodkazposlandne timestamp without time zone,
    discountcode text
);


ALTER TABLE public.objednavka_objektu OWNER TO postgres;

--
-- Name: objednavka_objektu_zak; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_objektu_zak (
    id text NOT NULL,
    poradi integer NOT NULL,
    cena numeric,
    pocet integer NOT NULL,
    zakaznikid text,
    tokenid text,
    objednavka_objektu text
);


ALTER TABLE public.objednavka_objektu_zak OWNER TO postgres;

--
-- Name: objednavka_prmanentky; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_prmanentky (
    id text NOT NULL,
    poradi integer NOT NULL,
    cena numeric,
    platbadepositem boolean NOT NULL,
    zakaznikid text,
    typpermanentkyid text,
    vydejkaid text,
    platnostod timestamp without time zone,
    objednavka text,
    discountcode text
);


ALTER TABLE public.objednavka_prmanentky OWNER TO postgres;

--
-- Name: objednavka_storno_poplatku; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_storno_poplatku (
    id text NOT NULL,
    objektid text,
    zadavatelid text,
    datumzadani timestamp without time zone,
    datumstorna timestamp without time zone,
    stornovalid text,
    objednavkaobjektuzakid text,
    jednotek numeric,
    cena numeric,
    platbadepositem boolean NOT NULL,
    poznamka text,
    objednavka text
);


ALTER TABLE public.objednavka_storno_poplatku OWNER TO postgres;

--
-- Name: objednavka_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_token (
    id text NOT NULL,
    tokenid text,
    active boolean NOT NULL,
    objednavkaid text,
    externalentranceitemid text,
    deactivatedtime timestamp without time zone
);


ALTER TABLE public.objednavka_token OWNER TO postgres;

--
-- Name: objednavka_touch_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_touch_table (
    id text NOT NULL,
    objednavkaid text,
    touchtableid text
);


ALTER TABLE public.objednavka_touch_table OWNER TO postgres;

--
-- Name: objednavka_zbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_zbozi (
    id text NOT NULL,
    poradi integer NOT NULL,
    cena numeric,
    vydejkaid text,
    objednavka text,
    platbadepositem boolean NOT NULL,
    jednotek numeric,
    cenazajednotku numeric,
    sazbadph numeric,
    zboziid text,
    skladid text,
    platbapermanentkou boolean DEFAULT false,
    zalohaproobjednavkuobjektuid text,
    poznamka text,
    cislovoucheru text,
    discountcode text
);


ALTER TABLE public.objednavka_zbozi OWNER TO postgres;

--
-- Name: objednavka_zbozi_printed; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objednavka_zbozi_printed (
    id text NOT NULL,
    jednotek numeric,
    zboziid text,
    skladid text,
    tiskarna text,
    sablona text,
    casodeslani timestamp without time zone,
    zakaznikid text,
    touchtableid text,
    odesilatelid text,
    objednavkazboziid text,
    objednavkaid text,
    poznamka text
);


ALTER TABLE public.objednavka_zbozi_printed OWNER TO postgres;

--
-- Name: objekt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objekt (
    id text NOT NULL,
    kapacita integer NOT NULL,
    casovajednotka integer NOT NULL,
    typrezervace integer NOT NULL,
    primyvstup boolean NOT NULL,
    areal text,
    mindelkarezervace integer,
    maxdelkarezervace integer,
    volnopredrezervaci integer,
    volnoporezervaci integer,
    zarovnatzacatekrezervace integer,
    delkarezervacenasobkem integer,
    vicestavovy boolean,
    stav integer,
    reservationstart bytea,
    reservationfinish bytea,
    odcitatprocedury boolean DEFAULT false,
    rezervacenatokeny boolean DEFAULT false,
    rucniuzavrenivstupu boolean DEFAULT false,
    pozastavitvstup boolean DEFAULT false,
    showprogress boolean DEFAULT false,
    googlecalendarid text,
    googlecalendarnotification boolean DEFAULT false,
    googlecalendarnotificationbefore integer DEFAULT 0,
    checktokenscount boolean DEFAULT false,
    selectinstructor boolean DEFAULT false,
    showinstructorname boolean DEFAULT false,
    showsportname boolean DEFAULT false,
    upravenicasuvstupu boolean DEFAULT true,
    vytvorenirezervacepredzacatkem integer,
    editacerezervacepredzacatkem integer,
    zrusenirezervacepredzacatkem integer
);


ALTER TABLE public.objekt OWNER TO postgres;

--
-- Name: objekt_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objekt_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    objekt text,
    zkracenynazev text
);


ALTER TABLE public.objekt_loc OWNER TO postgres;

--
-- Name: objekt_sport; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objekt_sport (
    objekt text,
    sport text,
    id text NOT NULL,
    index integer DEFAULT 0
);


ALTER TABLE public.objekt_sport OWNER TO postgres;

--
-- Name: obsazeni_objektu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.obsazeni_objektu (
    id text NOT NULL,
    typ integer NOT NULL,
    zacatek timestamp without time zone,
    konec timestamp without time zone,
    pocet integer NOT NULL,
    objektid text,
    active boolean NOT NULL,
    objednavka_objektu text,
    blokace_objektu text,
    rzacatek timestamp without time zone,
    rkonec timestamp without time zone,
    otevrene boolean NOT NULL,
    counter integer DEFAULT 0,
    vstup boolean,
    hlavni_obsazeni text,
    hasparent boolean DEFAULT false
);


ALTER TABLE public.obsazeni_objektu OWNER TO postgres;

--
-- Name: odvod; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.odvod (
    cislo text NOT NULL,
    datumvystaveni timestamp without time zone,
    datumstorna timestamp without time zone,
    zadavatelid text,
    stornovalid text,
    mena text,
    cena numeric,
    vycetka bytea,
    poznamka text,
    pokladna text,
    cenapoukazky numeric,
    vycetkapoukazek bytea,
    vyberzpokladny boolean DEFAULT false,
    regpokladnaid text,
    ekasauid text,
    ekasanumber text,
    ekasaokp text
);


ALTER TABLE public.odvod OWNER TO postgres;

--
-- Name: omezeni_rezervaci; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.omezeni_rezervaci (
    objektid text NOT NULL,
    omezeni bytea
);


ALTER TABLE public.omezeni_rezervaci OWNER TO postgres;

--
-- Name: osoba; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.osoba (
    id text NOT NULL,
    jmeno text,
    prijmeni text,
    titul text,
    telefon text,
    email text,
    ulice text,
    psc text,
    mesto text,
    foto bytea,
    poznamka text,
    ico text,
    dic text,
    mobil text,
    web text,
    kontaktniosoba text,
    banka text,
    ucet text,
    datumnarozeni date,
    blokace boolean DEFAULT false,
    domovskyklub boolean DEFAULT false,
    pohlavi boolean,
    jazykkomunikace text,
    handicap double precision,
    cisloprefix text,
    cislosuffix text,
    golferid integer,
    emailaktivni boolean DEFAULT true,
    telefonaktivni boolean DEFAULT true,
    predvolba text DEFAULT '+420'::text,
    zeme text,
    icdph text,
    golfcutid text,
    googlecalendarid text,
    googlecalendarnotification boolean DEFAULT false,
    googlecalendarnotificationbefore integer DEFAULT 0
);


ALTER TABLE public.osoba OWNER TO postgres;

--
-- Name: otviraci_doba; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.otviraci_doba (
    objektid text NOT NULL,
    platnostod timestamp without time zone NOT NULL,
    otviracidoba bytea
);


ALTER TABLE public.otviraci_doba OWNER TO postgres;

--
-- Name: ovladac_objektu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ovladac_objektu (
    id text NOT NULL,
    idovladace text,
    manual boolean,
    automat boolean,
    zapnutipredzacatkem integer NOT NULL,
    cislazapojeni text,
    objektid text,
    delkasepnutipokonci integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.ovladac_objektu OWNER TO postgres;

--
-- Name: ovladani_objektu_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ovladani_objektu_log (
    id text NOT NULL,
    objektid text,
    cinnostid text,
    datumspusteni timestamp without time zone,
    datumvypnuti timestamp without time zone,
    objednavkaid text
);


ALTER TABLE public.ovladani_objektu_log OWNER TO postgres;

--
-- Name: ovladani_objektu_quido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ovladani_objektu_quido (
    id text NOT NULL,
    nazev text,
    ip text,
    port integer,
    pocetvstupu integer,
    pocetvystupu integer,
    mapovanivstupu bytea
);


ALTER TABLE public.ovladani_objektu_quido OWNER TO postgres;

--
-- Name: ovladani_objektu_server; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ovladani_objektu_server (
    id text NOT NULL,
    ovladace bytea,
    ovladacesettings bytea
);


ALTER TABLE public.ovladani_objektu_server OWNER TO postgres;

--
-- Name: ovladani_vstupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ovladani_vstupu (
    id text NOT NULL,
    nazev text,
    connectionparameters bytea,
    idovladacevcs integer,
    idzarizeni text
);


ALTER TABLE public.ovladani_vstupu OWNER TO postgres;

--
-- Name: ovladani_vstupu_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ovladani_vstupu_log (
    id text NOT NULL,
    datum timestamp without time zone,
    nastavenivstupuid text,
    zakaznikid text,
    tokenid text,
    objednavkaobjektuid text,
    podminkauspesna boolean,
    akceuspesna boolean,
    ucetosobajmenoprijmeni text,
    podminka text
);


ALTER TABLE public.ovladani_vstupu_log OWNER TO postgres;

--
-- Name: ovladani_vstupu_reader; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ovladani_vstupu_reader (
    id text NOT NULL,
    nazev text,
    ip text,
    port integer,
    idovladacevcs integer,
    ovladani_vstupu text
);


ALTER TABLE public.ovladani_vstupu_reader OWNER TO postgres;

--
-- Name: permanentka; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permanentka (
    id text NOT NULL,
    objednavkapermanentkyid text,
    zakaznikid text,
    jednotek numeric,
    inicialnistav numeric,
    platnostod timestamp without time zone,
    platnostdo timestamp without time zone,
    casovaomezeni bytea,
    cinnostiid bytea,
    zboziid bytea,
    permanentkacinnost text,
    permanentkazbozi text,
    typpermanentky text
);


ALTER TABLE public.permanentka OWNER TO postgres;

--
-- Name: permanentka_cinnost; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permanentka_cinnost (
    id text NOT NULL,
    jednotek numeric,
    nastaveniplatnosti bytea,
    casovaomezeni bytea,
    omezenicerpanijednotek numeric,
    omezenicerpaniperioda text,
    typ text
);


ALTER TABLE public.permanentka_cinnost OWNER TO postgres;

--
-- Name: permanentka_cinnosti; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permanentka_cinnosti (
    cinnost text NOT NULL,
    permanentka text NOT NULL
);


ALTER TABLE public.permanentka_cinnosti OWNER TO postgres;

--
-- Name: permanentka_zbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permanentka_zbozi (
    id text NOT NULL,
    jednotek numeric,
    nastaveniplatnosti bytea,
    casovaomezeni bytea,
    typ text,
    omezenicerpanijednotek numeric,
    omezenicerpaniperioda text
);


ALTER TABLE public.permanentka_zbozi OWNER TO postgres;

--
-- Name: permanentka_zbozi_list; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permanentka_zbozi_list (
    permanentka text NOT NULL,
    zbozi text NOT NULL
);


ALTER TABLE public.permanentka_zbozi_list OWNER TO postgres;

--
-- Name: platba_poukazkou; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.platba_poukazkou (
    id text NOT NULL,
    typpoukazky text,
    castka numeric,
    ucet text
);


ALTER TABLE public.platba_poukazkou OWNER TO postgres;

--
-- Name: platba_za_clenstvi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.platba_za_clenstvi (
    id text NOT NULL,
    zacatekplatnosti timestamp without time zone,
    cislovypisu text,
    datumzadani timestamp without time zone,
    zadal text,
    datumstorna timestamp without time zone,
    stornoval text,
    poznamka text
);


ALTER TABLE public.platba_za_clenstvi OWNER TO postgres;

--
-- Name: platba_za_clenstvi_zak; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.platba_za_clenstvi_zak (
    id text NOT NULL,
    zakazniktext text,
    variabilnisymbol text,
    datumuhrady text,
    castka text,
    zakaznikid text,
    objednavkaid text,
    platba_za_clenstvi text
);


ALTER TABLE public.platba_za_clenstvi_zak OWNER TO postgres;

--
-- Name: podm_vs_nulovy_ucet_pokladny; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.podm_vs_nulovy_ucet_pokladny (
    podminkavstupu text NOT NULL,
    pokladna text NOT NULL
);


ALTER TABLE public.podm_vs_nulovy_ucet_pokladny OWNER TO postgres;

--
-- Name: podm_vy_nulovy_ucet_pokladny; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.podm_vy_nulovy_ucet_pokladny (
    podminkavystupu text NOT NULL,
    pokladna text NOT NULL
);


ALTER TABLE public.podm_vy_nulovy_ucet_pokladny OWNER TO postgres;

--
-- Name: podminka_rezervace; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.podminka_rezervace (
    id text NOT NULL,
    priorita bigint NOT NULL,
    objektrezervaceid text,
    objektrezervaceobsazen boolean,
    objektid text,
    name text
);


ALTER TABLE public.podminka_rezervace OWNER TO postgres;

--
-- Name: podminka_vstupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.podminka_vstupu (
    id text NOT NULL,
    nazev text,
    skupinaid text,
    skupinaidset boolean,
    pohlavi boolean,
    objektid text,
    arealid text,
    tolerance integer,
    limitprihlaseni integer,
    priorita integer,
    ipctecky text,
    nulovyucet boolean NOT NULL,
    minimalnideposit numeric,
    depositprocinnost text,
    limitproopusteni integer,
    maskrinku boolean NOT NULL,
    pozastavenyvstupobjektid text,
    zaplacenarezervace boolean NOT NULL,
    nastaveni_vstupu text,
    casoveomezeni text,
    neuhrazenesplatky integer
);


ALTER TABLE public.podminka_vstupu OWNER TO postgres;

--
-- Name: pohyb_depositu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pohyb_depositu (
    id text NOT NULL,
    typ integer NOT NULL,
    cas timestamp without time zone,
    dokladid text,
    jednotek numeric,
    stav numeric,
    typplatby integer NOT NULL,
    deposit text,
    platnostdo timestamp without time zone,
    serial integer DEFAULT nextval(('pohyb_depositu_serial'::text)::regclass),
    pouzenakuphotovosti boolean,
    stavbezdph numeric,
    jednotekbezdph numeric,
    slozenipromozadavatelid text
);


ALTER TABLE public.pohyb_depositu OWNER TO postgres;

--
-- Name: pohyb_depositu_serial; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pohyb_depositu_serial
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pohyb_depositu_serial OWNER TO postgres;

--
-- Name: pohyb_na_pokladne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pohyb_na_pokladne (
    id text NOT NULL,
    typ integer,
    cas timestamp without time zone,
    dokladid text,
    cena numeric,
    stav numeric,
    zbozi text,
    zadavatelid text,
    pokladna text,
    serial integer DEFAULT nextval(('pohyb_na_pokladne_serial'::text)::regclass),
    stav2 numeric
);


ALTER TABLE public.pohyb_na_pokladne OWNER TO postgres;

--
-- Name: pohyb_na_pokladne_serial; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pohyb_na_pokladne_serial
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pohyb_na_pokladne_serial OWNER TO postgres;

--
-- Name: pohyb_permanentky; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pohyb_permanentky (
    id text NOT NULL,
    typ integer NOT NULL,
    cas timestamp without time zone,
    objednavkaobjektuid text,
    jednotek numeric,
    permanentka text,
    objednavkazboziid text,
    pouzitonacas timestamp without time zone
);


ALTER TABLE public.pohyb_permanentky OWNER TO postgres;

--
-- Name: pohyb_zbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pohyb_zbozi (
    id text NOT NULL,
    typ integer,
    cas timestamp without time zone,
    dokladid text,
    jednotek numeric,
    cena numeric,
    sazbadph numeric,
    stav numeric,
    polozka_vydejky text,
    sklad text,
    zbozi text,
    polozka_prijemky text,
    skladovacena numeric,
    serial integer DEFAULT nextval(('pohyb_zbozi_serial'::text)::regclass),
    storno boolean DEFAULT false,
    naslednypohyb boolean DEFAULT false
);


ALTER TABLE public.pohyb_zbozi OWNER TO postgres;

--
-- Name: pohyb_zbozi_serial; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pohyb_zbozi_serial
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pohyb_zbozi_serial OWNER TO postgres;

--
-- Name: pokladna; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pokladna (
    id text NOT NULL,
    stav1 numeric,
    stav2 numeric,
    minstav numeric,
    maxstav numeric,
    mena text,
    viditelna boolean NOT NULL,
    prepocetmen text,
    provozovna text,
    sekvence text,
    prohotovostniautomat boolean DEFAULT false
);


ALTER TABLE public.pokladna OWNER TO postgres;

--
-- Name: pokladna_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pokladna_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    pokladna text
);


ALTER TABLE public.pokladna_loc OWNER TO postgres;

--
-- Name: polozka_inventury; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.polozka_inventury (
    id text NOT NULL,
    zboziid text,
    skladovacena numeric,
    dph numeric,
    evidovanemnozstvi numeric,
    realnemnozstvi numeric,
    jednotkaid text,
    inventura text
);


ALTER TABLE public.polozka_inventury OWNER TO postgres;

--
-- Name: polozka_prijemky; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.polozka_prijemky (
    id text NOT NULL,
    poradi integer NOT NULL,
    jednotek numeric,
    nakupnicena numeric,
    sazbadph numeric,
    zbozi text,
    prijemka text,
    datumexpirace timestamp without time zone
);


ALTER TABLE public.polozka_prijemky OWNER TO postgres;

--
-- Name: polozka_slozeneho_zbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.polozka_slozeneho_zbozi (
    id text NOT NULL,
    jednotek numeric,
    pod_zbozi text,
    slozene_zbozi text
);


ALTER TABLE public.polozka_slozeneho_zbozi OWNER TO postgres;

--
-- Name: polozka_uctu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.polozka_uctu (
    id text NOT NULL,
    poradi integer NOT NULL,
    jednotek numeric,
    cenazajednotku numeric,
    sazbadph numeric,
    zbozi text,
    ucet text,
    datumrealizace timestamp without time zone,
    platbadepositem boolean NOT NULL,
    sklad text,
    datumkoncerealizace timestamp without time zone,
    cislovoucheru text,
    discountcode text
);


ALTER TABLE public.polozka_uctu OWNER TO postgres;

--
-- Name: polozka_vydejky; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.polozka_vydejky (
    id text NOT NULL,
    poradi integer NOT NULL,
    jednotek numeric,
    prodejnicena numeric,
    sazbadph numeric,
    zbozi text,
    vydejka text,
    skladovacena numeric
);


ALTER TABLE public.polozka_vydejky OWNER TO postgres;

--
-- Name: polozkasazbydph; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.polozkasazbydph (
    id text NOT NULL,
    platnostod timestamp without time zone NOT NULL,
    sazbadphid text,
    sazba numeric NOT NULL
);


ALTER TABLE public.polozkasazbydph OWNER TO postgres;

--
-- Name: prepocet_men; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prepocet_men (
    id text NOT NULL,
    mena text,
    kurz numeric,
    platnostod timestamp without time zone,
    platnostdo timestamp without time zone
);


ALTER TABLE public.prepocet_men OWNER TO postgres;

--
-- Name: prerusene_clenstvi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prerusene_clenstvi (
    id text NOT NULL,
    sequence integer,
    zakaznikid text,
    preruseniod timestamp without time zone,
    prerusenido timestamp without time zone,
    preruseneclenstvi bytea
);


ALTER TABLE public.prerusene_clenstvi OWNER TO postgres;

--
-- Name: prerusene_permanentky; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prerusene_permanentky (
    id text NOT NULL,
    sequence integer,
    zakaznikid text,
    preruseniod timestamp without time zone,
    prerusenido timestamp without time zone,
    prerusenepermanentky bytea
);


ALTER TABLE public.prerusene_permanentky OWNER TO postgres;

--
-- Name: prevodka; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prevodka (
    id text NOT NULL,
    caszadani timestamp without time zone,
    zadavatelid text,
    storno boolean NOT NULL,
    casstorna timestamp without time zone,
    stornovalid text,
    poznamka text,
    prijemka text,
    vydejka text
);


ALTER TABLE public.prevodka OWNER TO postgres;

--
-- Name: prijemka; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prijemka (
    id text NOT NULL,
    cena numeric,
    cenabezdph numeric,
    casnaskladneni timestamp without time zone,
    skladnikid text,
    storno boolean NOT NULL,
    casstorna timestamp without time zone,
    stornovalid text,
    poznamka text,
    sklad text,
    dodavatel text,
    typ integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.prijemka OWNER TO postgres;

--
-- Name: print_template; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.print_template (
    id text NOT NULL,
    content text,
    type integer NOT NULL,
    templatename text,
    filename text
);


ALTER TABLE public.print_template OWNER TO postgres;

--
-- Name: provozovna; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.provozovna (
    id text NOT NULL,
    subjekt text,
    nazev text,
    eetid text
);


ALTER TABLE public.provozovna OWNER TO postgres;

--
-- Name: push_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.push_history (
    id text NOT NULL,
    uzivatelid text,
    read boolean DEFAULT false,
    removed boolean DEFAULT false,
    pushmulticast text
);


ALTER TABLE public.push_history OWNER TO postgres;

--
-- Name: push_key; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.push_key (
    id text NOT NULL,
    key text,
    uzivatel_id text
);


ALTER TABLE public.push_key OWNER TO postgres;

--
-- Name: push_multicast; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.push_multicast (
    id text NOT NULL,
    groups bytea,
    date timestamp without time zone,
    title text,
    body text,
    automatic boolean DEFAULT false,
    sent boolean DEFAULT false,
    historybody text
);


ALTER TABLE public.push_multicast OWNER TO postgres;

--
-- Name: remote_order_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.remote_order_log (
    id text NOT NULL,
    date timestamp without time zone,
    ip text,
    request text,
    response text,
    objednavkaid text,
    confirmed boolean NOT NULL
);


ALTER TABLE public.remote_order_log OWNER TO postgres;

--
-- Name: remote_order_zbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.remote_order_zbozi (
    id text NOT NULL,
    alias text,
    zboziid text
);


ALTER TABLE public.remote_order_zbozi OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id text NOT NULL,
    nazev text,
    popis text
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: rychle_volby_historie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rychle_volby_historie (
    id text NOT NULL,
    datudalosti timestamp without time zone,
    login text,
    nazevovladace text,
    nazevtlacitka text,
    uspech boolean,
    idzarizeni text,
    instrukce text,
    ip text,
    port text
);


ALTER TABLE public.rychle_volby_historie OWNER TO postgres;

--
-- Name: sazbadph; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sazbadph (
    id text NOT NULL,
    nazev text NOT NULL,
    symbol text
);


ALTER TABLE public.sazbadph OWNER TO postgres;

--
-- Name: seq; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seq (
    name text NOT NULL,
    pattern text,
    minvalue integer NOT NULL,
    last text,
    type integer,
    stornoseq text
);


ALTER TABLE public.seq OWNER TO postgres;

--
-- Name: sklad; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sklad (
    id text NOT NULL
);


ALTER TABLE public.sklad OWNER TO postgres;

--
-- Name: sklad_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sklad_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    sklad text
);


ALTER TABLE public.sklad_loc OWNER TO postgres;

--
-- Name: sklad_seq; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sklad_seq (
    sklad text NOT NULL,
    sekvence text NOT NULL
);


ALTER TABLE public.sklad_seq OWNER TO postgres;

--
-- Name: sklad_skupina; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sklad_skupina (
    sklad text NOT NULL,
    skupina text NOT NULL
);


ALTER TABLE public.sklad_skupina OWNER TO postgres;

--
-- Name: skupina; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.skupina (
    id text NOT NULL,
    nazev text,
    nastaveni bytea
);


ALTER TABLE public.skupina OWNER TO postgres;

--
-- Name: skupina_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.skupina_role (
    role text NOT NULL,
    skupina text NOT NULL
);


ALTER TABLE public.skupina_role OWNER TO postgres;

--
-- Name: skupina_zakazniku; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.skupina_zakazniku (
    id text NOT NULL,
    index integer NOT NULL,
    barvapopredi bytea,
    barvapozadi bytea,
    cenajenzdepositu boolean NOT NULL,
    platnostod timestamp without time zone,
    platnostdo timestamp without time zone,
    golfclubs bytea,
    viditelna boolean,
    zobrazovatsmlouvu boolean DEFAULT false NOT NULL,
    viditelnaweb boolean DEFAULT true
);


ALTER TABLE public.skupina_zakazniku OWNER TO postgres;

--
-- Name: skupina_zakazniku_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.skupina_zakazniku_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    skupina text
);


ALTER TABLE public.skupina_zakazniku_loc OWNER TO postgres;

--
-- Name: skupina_zakazniku_sazba; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.skupina_zakazniku_sazba (
    id text NOT NULL,
    timeamount numeric,
    timeunit integer,
    zboziid text,
    typsplatky text,
    dueday integer,
    specifickysymbol text,
    visible boolean,
    skupinazakazniku text
);


ALTER TABLE public.skupina_zakazniku_sazba OWNER TO postgres;

--
-- Name: slevovy_kod_skupiny; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.slevovy_kod_skupiny (
    id text NOT NULL,
    kod text,
    jednorazovy boolean NOT NULL,
    platnostod timestamp without time zone,
    platnostdo timestamp without time zone,
    skupinazakazniku text
);


ALTER TABLE public.slevovy_kod_skupiny OWNER TO postgres;

--
-- Name: slevovy_kod_skupiny_zakaznik; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.slevovy_kod_skupiny_zakaznik (
    id text NOT NULL,
    kod text,
    zakaznikid text,
    objednavkaid text,
    datumuplatneni timestamp without time zone
);


ALTER TABLE public.slevovy_kod_skupiny_zakaznik OWNER TO postgres;

--
-- Name: sms_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_history (
    id text NOT NULL,
    date timestamp without time zone,
    message text,
    groups json,
    recipients json,
    morerecipients json,
    automatic boolean
);


ALTER TABLE public.sms_history OWNER TO postgres;

--
-- Name: splatka_clenstvi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.splatka_clenstvi (
    id text NOT NULL,
    poradi integer NOT NULL,
    cena numeric,
    datumsplatnosti timestamp without time zone,
    datumzaplaceni timestamp without time zone,
    zaobdobiod timestamp without time zone,
    zaobdobido timestamp without time zone,
    objednavkaclenstviid text,
    platbazaclenstvizakid text,
    kalendar_splatek text,
    datumstornovaniplatby timestamp without time zone,
    poznamka text
);


ALTER TABLE public.splatka_clenstvi OWNER TO postgres;

--
-- Name: sport; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sport (
    id text NOT NULL,
    typ integer NOT NULL,
    zboziid text,
    sazbajednotek integer,
    sazbanaosobu boolean NOT NULL,
    sazbanacas integer,
    podsportycount integer NOT NULL,
    sazbystorna bytea,
    nadrazeny_sport text,
    mindelkarezervace integer,
    maxdelkarezervace integer,
    objednavkazaplniobjekt boolean NOT NULL,
    delkarezervacenasobkem integer,
    barvapopredi bytea,
    barvapozadi bytea,
    zobrazittext boolean NOT NULL,
    navazujici_sport text,
    navrezervaceoffset integer,
    delkahlavnirez integer,
    skladid text,
    activity_id text,
    uctovatzalohu boolean DEFAULT false,
    sport_kategorie text,
    minimalnipocetosob integer,
    minutypredvyhodnocenimkapacity integer,
    maximalnipocetosobnazakaznika integer,
    viditelnyweb boolean DEFAULT true
);


ALTER TABLE public.sport OWNER TO postgres;

--
-- Name: sport_instructor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sport_instructor (
    id text NOT NULL,
    activityid text,
    oldsportid text,
    deleted boolean NOT NULL,
    sport_id text,
    instructor_id text
);


ALTER TABLE public.sport_instructor OWNER TO postgres;

--
-- Name: sport_kategorie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sport_kategorie (
    id text NOT NULL,
    nadrazena_kategorie text,
    multisportfacilityid text,
    multisportserviceuuid text
);


ALTER TABLE public.sport_kategorie OWNER TO postgres;

--
-- Name: sport_kategorie_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sport_kategorie_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    sportkategorie text
);


ALTER TABLE public.sport_kategorie_loc OWNER TO postgres;

--
-- Name: sport_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sport_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    sport text
);


ALTER TABLE public.sport_loc OWNER TO postgres;

--
-- Name: statecode; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.statecode (
    id text NOT NULL,
    state text NOT NULL,
    phonepreset text NOT NULL,
    intcode text,
    fulltitle text
);


ALTER TABLE public.statecode OWNER TO postgres;

--
-- Name: stav_skladu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stav_skladu (
    id text NOT NULL,
    mnozstvi numeric,
    zbozi text,
    sklad text,
    minimalnimnozstvi numeric,
    minimalnirabat numeric,
    skladovacena numeric,
    nakupnicenavychozi numeric
);


ALTER TABLE public.stav_skladu OWNER TO postgres;

--
-- Name: table_category_usergroup; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.table_category_usergroup (
    usergroup_id text NOT NULL,
    category_id text NOT NULL
);


ALTER TABLE public.table_category_usergroup OWNER TO postgres;

--
-- Name: template_html_note; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.template_html_note (
    id text NOT NULL,
    header text,
    content text,
    index integer NOT NULL
);


ALTER TABLE public.template_html_note OWNER TO postgres;

--
-- Name: terminal_payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.terminal_payment (
    id text NOT NULL,
    createdpaymentid text,
    confirmedpaymentid text,
    confirmedpaymentdate timestamp without time zone,
    stornopaymentid text,
    stornopaymentdate timestamp without time zone,
    objednavkaid text
);


ALTER TABLE public.terminal_payment OWNER TO postgres;

--
-- Name: token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.token (
    id text NOT NULL,
    typ text,
    popis text
);


ALTER TABLE public.token OWNER TO postgres;

--
-- Name: token_confirmation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.token_confirmation (
    id text NOT NULL,
    cas timestamp without time zone,
    zakaznikid text,
    uzivatelid text,
    tokenid text,
    confirmation boolean NOT NULL
);


ALTER TABLE public.token_confirmation OWNER TO postgres;

--
-- Name: touch_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_category (
    id text NOT NULL,
    name text,
    iconid text,
    priority bigint NOT NULL
);


ALTER TABLE public.touch_category OWNER TO postgres;

--
-- Name: touch_category_usergroup; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_category_usergroup (
    usergroup_id text NOT NULL,
    category_id text NOT NULL
);


ALTER TABLE public.touch_category_usergroup OWNER TO postgres;

--
-- Name: touch_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_product (
    id text NOT NULL,
    name text,
    zboziid text,
    skladid text,
    priority bigint NOT NULL
);


ALTER TABLE public.touch_product OWNER TO postgres;

--
-- Name: touch_product_touch_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_product_touch_category (
    product_id text NOT NULL,
    category_id text NOT NULL
);


ALTER TABLE public.touch_product_touch_category OWNER TO postgres;

--
-- Name: touch_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_table (
    id text NOT NULL,
    active boolean NOT NULL,
    parent_table text,
    priority bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.touch_table OWNER TO postgres;

--
-- Name: touch_table_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_table_category (
    id text NOT NULL,
    name text,
    priority bigint NOT NULL
);


ALTER TABLE public.touch_table_category OWNER TO postgres;

--
-- Name: touch_table_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_table_loc (
    id text NOT NULL,
    locale text,
    name text,
    description text,
    touch_table text
);


ALTER TABLE public.touch_table_loc OWNER TO postgres;

--
-- Name: touch_table_touch_table_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.touch_table_touch_table_category (
    table_id text NOT NULL,
    category_id text NOT NULL
);


ALTER TABLE public.touch_table_touch_table_category OWNER TO postgres;

--
-- Name: typ_depositu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_depositu (
    id text NOT NULL,
    prodejni_zbozi text,
    minimalnimnozstvi numeric,
    nastaveniexpirace bytea,
    uctovaci_zbozi text,
    uctovacipokladnaid text,
    zobrazovatsmlouvu boolean,
    umoznitzvyseni boolean,
    zvysenivychozi boolean,
    prodejni_zbozi_0 text,
    prodejni_zbozi_9 text,
    prodejni_zbozi_19 text,
    uctovatjakozaloha boolean,
    nakupuctovat9procent boolean DEFAULT false,
    automatickyexpirovat boolean DEFAULT false
);


ALTER TABLE public.typ_depositu OWNER TO postgres;

--
-- Name: typ_depositu_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_depositu_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    typ_depositu text
);


ALTER TABLE public.typ_depositu_loc OWNER TO postgres;

--
-- Name: typ_permanentky; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_permanentky (
    id text NOT NULL,
    nazev text,
    viditelny boolean,
    poradi integer,
    globalninastaveniplatnosti bytea,
    globalnicasovaomezeni bytea,
    prodejni_zbozi text,
    viditelnyweb boolean DEFAULT true
);


ALTER TABLE public.typ_permanentky OWNER TO postgres;

--
-- Name: typ_platebni_karty; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_platebni_karty (
    id text NOT NULL,
    nazev text,
    icon text
);


ALTER TABLE public.typ_platebni_karty OWNER TO postgres;

--
-- Name: typ_poukazek; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_poukazek (
    id text NOT NULL,
    nazev text,
    electronic boolean DEFAULT false
);


ALTER TABLE public.typ_poukazek OWNER TO postgres;

--
-- Name: typ_tokenu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_tokenu (
    id text NOT NULL,
    autoprirazovatzakaznikum boolean NOT NULL,
    zobrazovatnazevtokenu boolean DEFAULT true
);


ALTER TABLE public.typ_tokenu OWNER TO postgres;

--
-- Name: typ_tokenu_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_tokenu_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    typ_tokenu text
);


ALTER TABLE public.typ_tokenu_loc OWNER TO postgres;

--
-- Name: typ_tokenu_podminka_vstupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_tokenu_podminka_vstupu (
    typtokenu text NOT NULL,
    podminkavstupu text NOT NULL
);


ALTER TABLE public.typ_tokenu_podminka_vstupu OWNER TO postgres;

--
-- Name: typ_tokenu_podminka_vystupu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typ_tokenu_podminka_vystupu (
    podminkavystupu text NOT NULL,
    typtokenu text NOT NULL
);


ALTER TABLE public.typ_tokenu_podminka_vystupu OWNER TO postgres;

--
-- Name: typdeposituzbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.typdeposituzbozi (
    deposit text NOT NULL,
    zbozi text NOT NULL
);


ALTER TABLE public.typdeposituzbozi OWNER TO postgres;

--
-- Name: ucet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ucet (
    cislo text NOT NULL,
    datumvystaveni timestamp without time zone,
    datumstorna timestamp without time zone,
    zakaznikid text,
    zadavatelid text,
    stornovalid text,
    typplatby integer NOT NULL,
    mena text,
    cena numeric,
    poznamka text,
    pokladna text,
    cisloplatby text,
    platceobjednavky text,
    typplatebnikartyid text,
    regpokladnaid text,
    bkp text,
    fik text,
    pkp text,
    fiskalizovano numeric,
    guestname text,
    roomname text,
    ekasauid text,
    ekasanumber text,
    ekasaokp text,
    paragonnumber bigint,
    ekasaoffline boolean DEFAULT false,
    hoteltimeid bigint,
    unipayid text,
    stornoucetid text
);


ALTER TABLE public.ucet OWNER TO postgres;

--
-- Name: user_qr_access_code; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_qr_access_code (
    id text NOT NULL,
    code text,
    active boolean NOT NULL,
    createdat timestamp without time zone,
    deactivatedat timestamp without time zone,
    deactivatedby text,
    uzivatel_id text
);


ALTER TABLE public.user_qr_access_code OWNER TO postgres;

--
-- Name: uzaverka_reg_pokladny; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uzaverka_reg_pokladny (
    id text NOT NULL,
    datum timestamp without time zone,
    typ integer NOT NULL,
    pokladna text
);


ALTER TABLE public.uzaverka_reg_pokladny OWNER TO postgres;

--
-- Name: uzivatel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uzivatel (
    login text NOT NULL,
    heslo text,
    skupina text,
    nastaveni bytea,
    jmeno text,
    auth_key text,
    aktivni boolean DEFAULT true NOT NULL,
    aktivacnitoken text
);


ALTER TABLE public.uzivatel OWNER TO postgres;

--
-- Name: uzivatel_login_attempt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uzivatel_login_attempt (
    id text NOT NULL,
    login character varying(255) NOT NULL,
    created timestamp without time zone NOT NULL,
    blockedtilltime timestamp without time zone,
    ip character varying(15) NOT NULL
);


ALTER TABLE public.uzivatel_login_attempt OWNER TO postgres;

--
-- Name: uzivatel_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uzivatel_role (
    id text NOT NULL,
    login text NOT NULL
);


ALTER TABLE public.uzivatel_role OWNER TO postgres;

--
-- Name: uzivatel_session; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uzivatel_session (
    id text NOT NULL,
    logintime timestamp without time zone,
    logouttime timestamp without time zone,
    logintype integer NOT NULL,
    logoutmethod integer NOT NULL,
    uzivatel text,
    ipaddress text,
    clientid text,
    lastactivetime timestamp without time zone
);


ALTER TABLE public.uzivatel_session OWNER TO postgres;

--
-- Name: uzivatel_session_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uzivatel_session_token (
    series text NOT NULL,
    username text,
    token text,
    lastused timestamp without time zone
);


ALTER TABLE public.uzivatel_session_token OWNER TO postgres;

--
-- Name: valid_registration_number; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.valid_registration_number (
    registrationnumber text NOT NULL
);


ALTER TABLE public.valid_registration_number OWNER TO postgres;

--
-- Name: version; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.version (
    id integer NOT NULL,
    revision integer NOT NULL,
    createddate timestamp without time zone DEFAULT now() NOT NULL,
    message text
);


ALTER TABLE public.version OWNER TO postgres;

--
-- Name: version_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.version_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.version_id_seq OWNER TO postgres;

--
-- Name: version_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.version_id_seq OWNED BY public.version.id;


--
-- Name: video_odkaz; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.video_odkaz (
    id text NOT NULL,
    objektid text,
    link text,
    start timestamp without time zone
);


ALTER TABLE public.video_odkaz OWNER TO postgres;

--
-- Name: vklad; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vklad (
    cislo text NOT NULL,
    datumvystaveni timestamp without time zone,
    datumstorna timestamp without time zone,
    zadavatelid text,
    stornovalid text,
    mena text,
    cena numeric,
    poznamka text,
    pokladna text,
    vkladnapokladnu boolean DEFAULT false,
    regpokladnaid text,
    ekasauid text,
    ekasanumber text,
    ekasaokp text,
    vycetka bytea
);


ALTER TABLE public.vklad OWNER TO postgres;

--
-- Name: voucher; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voucher (
    id text NOT NULL,
    cinnost text,
    pocet integer NOT NULL,
    cena numeric,
    mena text,
    obdobiod timestamp without time zone,
    obdobido timestamp without time zone,
    datumvytvoreni timestamp without time zone,
    zadavatelid text,
    jmeno text,
    zakaznikid text,
    zboziid text,
    zbozicount integer,
    poznamka text,
    sluzbaid text,
    sluzbacount integer,
    cenacelkem numeric,
    objednavkaid text,
    aktivni boolean DEFAULT true,
    objednavkazboziid text,
    expirovano boolean DEFAULT false,
    online boolean DEFAULT false,
    emailanonymnekupujiciho text,
    jmenoanonymnekupujiciho text,
    clenstviid text,
    balicekid text
);


ALTER TABLE public.voucher OWNER TO postgres;

--
-- Name: vstupni_akce; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vstupni_akce (
    id text NOT NULL,
    nazev text,
    oteviracipokladnaid text,
    uzavritucetnapokladne text,
    vpustitdozapojeni integer,
    zboziid text,
    skladid text,
    jednotekzbozi integer,
    zaplatitzbozinapokladne text,
    objektid text,
    sportid text,
    p4command text,
    displayline1 text,
    displayline2 text,
    line1suffixtype integer NOT NULL,
    line2suffixtype integer NOT NULL,
    displayid integer,
    platitpermanentkou boolean NOT NULL,
    spustitvstupvobjektu text,
    zastavitvstupvobjektu text,
    zobrazittokeninfokascomp boolean NOT NULL,
    manualopenzapojeni integer,
    podminka_vstupu text,
    vlozitrezervacinaucet boolean DEFAULT false,
    platitmultisportem boolean DEFAULT false,
    vlozitrezervacimultisportnaucet boolean DEFAULT false,
    uhraditplatbouhoteltime boolean DEFAULT false,
    displayp4command text,
    priorita bigint
);


ALTER TABLE public.vstupni_akce OWNER TO postgres;

--
-- Name: vydavac_micku_sluzba; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vydavac_micku_sluzba (
    id text NOT NULL,
    zboziid text,
    pocetmicku integer,
    platnostdni integer,
    limitvalue integer,
    limittimeunit integer
);


ALTER TABLE public.vydavac_micku_sluzba OWNER TO postgres;

--
-- Name: vydejka; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vydejka (
    id text NOT NULL,
    cena numeric,
    cenabezdph numeric,
    casvyskladneni timestamp without time zone,
    skladnikid text,
    odberatelid text,
    storno boolean NOT NULL,
    casstorna timestamp without time zone,
    stornovalid text,
    poznamka text,
    sklad text,
    typ integer NOT NULL
);


ALTER TABLE public.vydejka OWNER TO postgres;

--
-- Name: waiting_storno; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.waiting_storno (
    id text NOT NULL,
    objektid text,
    zacatek timestamp without time zone,
    konec timestamp without time zone,
    objednavkaobjektuzakid text
);


ALTER TABLE public.waiting_storno OWNER TO postgres;

--
-- Name: web_actuality; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.web_actuality (
    id text NOT NULL,
    date timestamp without time zone,
    header text,
    content text
);


ALTER TABLE public.web_actuality OWNER TO postgres;

--
-- Name: web_informace; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.web_informace (
    id text NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    header text,
    content text
);


ALTER TABLE public.web_informace OWNER TO postgres;

--
-- Name: web_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.web_message (
    id text NOT NULL,
    createddate timestamp without time zone,
    validfrom timestamp without time zone,
    validto timestamp without time zone,
    visible boolean NOT NULL,
    title text,
    message text,
    category text,
    messagetype text
);


ALTER TABLE public.web_message OWNER TO postgres;

--
-- Name: web_oznameni; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.web_oznameni (
    id text NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    header text,
    content text
);


ALTER TABLE public.web_oznameni OWNER TO postgres;

--
-- Name: zakaznik; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zakaznik (
    id text NOT NULL,
    data bytea,
    deposit text,
    osoba text,
    uzivatel text,
    aktivni boolean NOT NULL,
    viditelny boolean NOT NULL,
    zadal text,
    firma boolean,
    zakaznikparent text,
    counter integer,
    typ integer DEFAULT 0 NOT NULL,
    datumsouhlasuobchpodm timestamp without time zone,
    datumposlednizmeny timestamp without time zone DEFAULT now(),
    passwordtoken text
);


ALTER TABLE public.zakaznik OWNER TO postgres;

--
-- Name: zakaznik_changes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zakaznik_changes (
    id text NOT NULL,
    zakaznikid text,
    property integer NOT NULL,
    oldvalue text,
    newvalue text,
    datum timestamp without time zone,
    zadavatelid text
);


ALTER TABLE public.zakaznik_changes OWNER TO postgres;

--
-- Name: zakaznik_skupina_viditelnost; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zakaznik_skupina_viditelnost (
    zakaznik text NOT NULL,
    skupina text NOT NULL
);


ALTER TABLE public.zakaznik_skupina_viditelnost OWNER TO postgres;

--
-- Name: zakaznik_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zakaznik_token (
    id text NOT NULL,
    casod timestamp without time zone,
    casdo timestamp without time zone,
    zakaznik text,
    token text
);


ALTER TABLE public.zakaznik_token OWNER TO postgres;

--
-- Name: zbozi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zbozi (
    id text NOT NULL,
    vlozeno timestamp without time zone,
    jednotka text,
    typ integer NOT NULL,
    pocetporci numeric
);


ALTER TABLE public.zbozi OWNER TO postgres;

--
-- Name: zbozi_ean; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zbozi_ean (
    id text NOT NULL,
    carovykod text NOT NULL,
    typ text NOT NULL,
    zboziid text NOT NULL
);


ALTER TABLE public.zbozi_ean OWNER TO postgres;

--
-- Name: zbozi_kategorie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zbozi_kategorie (
    zbozi text NOT NULL,
    kategorie text NOT NULL
);


ALTER TABLE public.zbozi_kategorie OWNER TO postgres;

--
-- Name: zbozi_loc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zbozi_loc (
    id text NOT NULL,
    jazyk text,
    nazev text,
    popis text,
    zbozi text
);


ALTER TABLE public.zbozi_loc OWNER TO postgres;

--
-- Name: version id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.version ALTER COLUMN id SET DEFAULT nextval('public.version_id_seq'::regclass);


--
-- Name: activity_favourite activity_favourite_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activity_favourite
    ADD CONSTRAINT activity_favourite_pkey PRIMARY KEY (id);


--
-- Name: uzivatel auth_key_idx; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzivatel
    ADD CONSTRAINT auth_key_idx UNIQUE (auth_key);


--
-- Name: eet_data eet_data_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eet_data
    ADD CONSTRAINT eet_data_pk PRIMARY KEY (id);


--
-- Name: fakturacni_subjekt fakturacni_subjekt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fakturacni_subjekt
    ADD CONSTRAINT fakturacni_subjekt_pkey PRIMARY KEY (id);


--
-- Name: nastaveni_json nastaveni_json_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nastaveni_json
    ADD CONSTRAINT nastaveni_json_pkey PRIMARY KEY (key);


--
-- Name: noteheader noteheader_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.noteheader
    ADD CONSTRAINT noteheader_pkey PRIMARY KEY (id);


--
-- Name: objednavka_objektu objednavka_objektu_kod; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_objektu
    ADD CONSTRAINT objednavka_objektu_kod UNIQUE (kod);


--
-- Name: objekt_sport objekt_sport_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objekt_sport
    ADD CONSTRAINT objekt_sport_id_key UNIQUE (id);


--
-- Name: objekt_sport objekt_sport_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objekt_sport
    ADD CONSTRAINT objekt_sport_pkey PRIMARY KEY (id);


--
-- Name: activity pk_activity; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT pk_activity PRIMARY KEY (id);


--
-- Name: activity_webtab pk_activity_webtab; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activity_webtab
    ADD CONSTRAINT pk_activity_webtab PRIMARY KEY (id);


--
-- Name: agreement pk_agreement; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.agreement
    ADD CONSTRAINT pk_agreement PRIMARY KEY (id);


--
-- Name: areal pk_areal; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.areal
    ADD CONSTRAINT pk_areal PRIMARY KEY (id);


--
-- Name: areal_loc pk_areal_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.areal_loc
    ADD CONSTRAINT pk_areal_loc PRIMARY KEY (id);


--
-- Name: automatic_attachment_template pk_automatic_attachment_template; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automatic_attachment_template
    ADD CONSTRAINT pk_automatic_attachment_template PRIMARY KEY (id);


--
-- Name: automatic_message pk_automatic_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automatic_message
    ADD CONSTRAINT pk_automatic_email PRIMARY KEY (id);


--
-- Name: benefity_change pk_benefity_change; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benefity_change
    ADD CONSTRAINT pk_benefity_change PRIMARY KEY (id);


--
-- Name: blokace_objektu pk_blokace_objektu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blokace_objektu
    ADD CONSTRAINT pk_blokace_objektu PRIMARY KEY (id);


--
-- Name: blokace_objektu_loc pk_blokace_objektu_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blokace_objektu_loc
    ADD CONSTRAINT pk_blokace_objektu_loc PRIMARY KEY (id);


--
-- Name: cashmatic_transaction pk_cashmatic_transaction; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cashmatic_transaction
    ADD CONSTRAINT pk_cashmatic_transaction PRIMARY KEY (id);


--
-- Name: casove_omezeni_vv pk_casove_omezeni_vv; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.casove_omezeni_vv
    ADD CONSTRAINT pk_casove_omezeni_vv PRIMARY KEY (id);


--
-- Name: cena pk_cena; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cena
    ADD CONSTRAINT pk_cena PRIMARY KEY (id);


--
-- Name: cinnost_permanentky_podminka_vstupu pk_cinnost_permanentky_podminka_vstupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cinnost_permanentky_podminka_vstupu
    ADD CONSTRAINT pk_cinnost_permanentky_podminka_vstupu PRIMARY KEY (sport, podminkavstupu);


--
-- Name: cinnost_permanentky_podminka_vystupu pk_cinnost_permanentky_podminka_vystupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cinnost_permanentky_podminka_vystupu
    ADD CONSTRAINT pk_cinnost_permanentky_podminka_vystupu PRIMARY KEY (podminkavystupu, sport);


--
-- Name: clen_skupiny_zakazniku pk_clen_skupiny_zakazniku; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clen_skupiny_zakazniku
    ADD CONSTRAINT pk_clen_skupiny_zakazniku PRIMARY KEY (id);


--
-- Name: consumption pk_consumption; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consumption
    ADD CONSTRAINT pk_consumption PRIMARY KEY (id);


--
-- Name: customers_agreement pk_customers_agreement; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customers_agreement
    ADD CONSTRAINT pk_customers_agreement PRIMARY KEY (id);


--
-- Name: delka_vstupu pk_delka_vstupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delka_vstupu
    ADD CONSTRAINT pk_delka_vstupu PRIMARY KEY (id);


--
-- Name: deposit pk_deposit; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deposit
    ADD CONSTRAINT pk_deposit PRIMARY KEY (id);


--
-- Name: document pk_document; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document
    ADD CONSTRAINT pk_document PRIMARY KEY (id);


--
-- Name: dodavatel pk_dodavatel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dodavatel
    ADD CONSTRAINT pk_dodavatel PRIMARY KEY (id);


--
-- Name: eet_history pk_eet_history; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eet_history
    ADD CONSTRAINT pk_eet_history PRIMARY KEY (id);


--
-- Name: ekasa_device pk_ekasa_device; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ekasa_device
    ADD CONSTRAINT pk_ekasa_device PRIMARY KEY (id);


--
-- Name: email_history pk_email_history; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_history
    ADD CONSTRAINT pk_email_history PRIMARY KEY (id);


--
-- Name: email_queue pk_email_queue; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_queue
    ADD CONSTRAINT pk_email_queue PRIMARY KEY (id);


--
-- Name: eplatba pk_eplatba; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eplatba
    ADD CONSTRAINT pk_eplatba PRIMARY KEY (id);


--
-- Name: expirace_zbozi pk_expirace_zbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expirace_zbozi
    ADD CONSTRAINT pk_expirace_zbozi PRIMARY KEY (id);


--
-- Name: external_client pk_external_client; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.external_client
    ADD CONSTRAINT pk_external_client PRIMARY KEY (id);


--
-- Name: external_entrance pk_external_entrance; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.external_entrance
    ADD CONSTRAINT pk_external_entrance PRIMARY KEY (id);


--
-- Name: external_entrance_item pk_external_entrance_item; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.external_entrance_item
    ADD CONSTRAINT pk_external_entrance_item PRIMARY KEY (id);


--
-- Name: external_entrance_log pk_external_entrance_log; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.external_entrance_log
    ADD CONSTRAINT pk_external_entrance_log PRIMARY KEY (id);


--
-- Name: faktura pk_faktura; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.faktura
    ADD CONSTRAINT pk_faktura PRIMARY KEY (id);


--
-- Name: generated_attachment pk_generated_attachment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.generated_attachment
    ADD CONSTRAINT pk_generated_attachment PRIMARY KEY (id);


--
-- Name: golf_club pk_golf_club; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.golf_club
    ADD CONSTRAINT pk_golf_club PRIMARY KEY (cislo);


--
-- Name: golf_hriste pk_golf_hriste; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.golf_hriste
    ADD CONSTRAINT pk_golf_hriste PRIMARY KEY (id);


--
-- Name: golf_odpaliste pk_golf_odpaliste; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.golf_odpaliste
    ADD CONSTRAINT pk_golf_odpaliste PRIMARY KEY (id);


--
-- Name: golf_odpaliste_typ pk_golf_odpaliste_typ; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.golf_odpaliste_typ
    ADD CONSTRAINT pk_golf_odpaliste_typ PRIMARY KEY (id);


--
-- Name: historie_nastaveni_cen pk_historie_nastaveni_cen; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historie_nastaveni_cen
    ADD CONSTRAINT pk_historie_nastaveni_cen PRIMARY KEY (id);


--
-- Name: historie_slev pk_historie_slev; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historie_slev
    ADD CONSTRAINT pk_historie_slev PRIMARY KEY (id);


--
-- Name: hromadna_objednavka_objektu pk_hromadna_objednavka_objektu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hromadna_objednavka_objektu
    ADD CONSTRAINT pk_hromadna_objednavka_objektu PRIMARY KEY (id);


--
-- Name: html_note pk_html_note; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.html_note
    ADD CONSTRAINT pk_html_note PRIMARY KEY (id);


--
-- Name: role pk_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT pk_id PRIMARY KEY (id);


--
-- Name: instructor pk_instructor; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.instructor
    ADD CONSTRAINT pk_instructor PRIMARY KEY (id);


--
-- Name: instructor_activity pk_instructor_activity; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.instructor_activity
    ADD CONSTRAINT pk_instructor_activity PRIMARY KEY (instructor_id, activity_id);


--
-- Name: inventura pk_inventura; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventura
    ADD CONSTRAINT pk_inventura PRIMARY KEY (id);


--
-- Name: jazyk pk_jazyk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jazyk
    ADD CONSTRAINT pk_jazyk PRIMARY KEY (id);


--
-- Name: jednotka pk_jednotka; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jednotka
    ADD CONSTRAINT pk_jednotka PRIMARY KEY (id);


--
-- Name: jednotka_loc pk_jednotka_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jednotka_loc
    ADD CONSTRAINT pk_jednotka_loc PRIMARY KEY (id);


--
-- Name: kalendar_splatek_clenstvi pk_kalendar_splatek_clenstvi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kalendar_splatek_clenstvi
    ADD CONSTRAINT pk_kalendar_splatek_clenstvi PRIMARY KEY (id);


--
-- Name: kascomp_device pk_kascomp_device; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kascomp_device
    ADD CONSTRAINT pk_kascomp_device PRIMARY KEY (id);


--
-- Name: kategorie pk_kategorie; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kategorie
    ADD CONSTRAINT pk_kategorie PRIMARY KEY (id);


--
-- Name: kategorie_fakturacnisubjekt pk_kategorie_fakturacnisubjekt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kategorie_fakturacnisubjekt
    ADD CONSTRAINT pk_kategorie_fakturacnisubjekt PRIMARY KEY (kategorie, fakturacnisubjekt);


--
-- Name: kategorie_loc pk_kategorie_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kategorie_loc
    ADD CONSTRAINT pk_kategorie_loc PRIMARY KEY (id);


--
-- Name: license pk_license; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.license
    ADD CONSTRAINT pk_license PRIMARY KEY (id);


--
-- Name: member_number pk_member_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member_number
    ADD CONSTRAINT pk_member_number PRIMARY KEY (id);


--
-- Name: mena pk_mena; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mena
    ADD CONSTRAINT pk_mena PRIMARY KEY (id);


--
-- Name: message_global_constraint pk_message_global_constraint; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_global_constraint
    ADD CONSTRAINT pk_message_global_constraint PRIMARY KEY (id);


--
-- Name: message_omezeni pk_message_omezeni; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_omezeni
    ADD CONSTRAINT pk_message_omezeni PRIMARY KEY (id);


--
-- Name: nadobjekt_objekt pk_nadobjekt_objekt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nadobjekt_objekt
    ADD CONSTRAINT pk_nadobjekt_objekt PRIMARY KEY (nadobjekt, objekt);


--
-- Name: nahradnik pk_nahradnik; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nahradnik
    ADD CONSTRAINT pk_nahradnik PRIMARY KEY (id);


--
-- Name: nasledne_douctovani pk_nasledne_douctovani; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nasledne_douctovani
    ADD CONSTRAINT pk_nasledne_douctovani PRIMARY KEY (id);


--
-- Name: nastaveni pk_nastaveni; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nastaveni
    ADD CONSTRAINT pk_nastaveni PRIMARY KEY (key);


--
-- Name: nastaveni_vstupu pk_nastaveni_vstupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nastaveni_vstupu
    ADD CONSTRAINT pk_nastaveni_vstupu PRIMARY KEY (id);


--
-- Name: oauth2_client_setting pk_oauth2_client_setting; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oauth2_client_setting
    ADD CONSTRAINT pk_oauth2_client_setting PRIMARY KEY (id);


--
-- Name: objednavka pk_objednavka; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka
    ADD CONSTRAINT pk_objednavka PRIMARY KEY (id);


--
-- Name: objednavka_clenstvi pk_objednavka_clenstvi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_clenstvi
    ADD CONSTRAINT pk_objednavka_clenstvi PRIMARY KEY (id);


--
-- Name: objednavka_depositu pk_objednavka_depositu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_depositu
    ADD CONSTRAINT pk_objednavka_depositu PRIMARY KEY (id);


--
-- Name: objednavka_obj_token pk_objednavka_obj_token; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_obj_token
    ADD CONSTRAINT pk_objednavka_obj_token PRIMARY KEY (token, objednavka_objektu);


--
-- Name: objednavka_objektu pk_objednavka_objektu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_objektu
    ADD CONSTRAINT pk_objednavka_objektu PRIMARY KEY (id);


--
-- Name: objednavka_objektu_zak pk_objednavka_objektu_zak; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_objektu_zak
    ADD CONSTRAINT pk_objednavka_objektu_zak PRIMARY KEY (id);


--
-- Name: objednavka_prmanentky pk_objednavka_prmanentky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_prmanentky
    ADD CONSTRAINT pk_objednavka_prmanentky PRIMARY KEY (id);


--
-- Name: objednavka_storno_poplatku pk_objednavka_storno_poplatku; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_storno_poplatku
    ADD CONSTRAINT pk_objednavka_storno_poplatku PRIMARY KEY (id);


--
-- Name: objednavka_token pk_objednavka_token; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_token
    ADD CONSTRAINT pk_objednavka_token PRIMARY KEY (id);


--
-- Name: objednavka_touch_table pk_objednavka_touch_table; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_touch_table
    ADD CONSTRAINT pk_objednavka_touch_table PRIMARY KEY (id);


--
-- Name: objednavka_zbozi pk_objednavka_zbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_zbozi
    ADD CONSTRAINT pk_objednavka_zbozi PRIMARY KEY (id);


--
-- Name: objednavka_zbozi_printed pk_objednavka_zbozi_printed; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objednavka_zbozi_printed
    ADD CONSTRAINT pk_objednavka_zbozi_printed PRIMARY KEY (id);


--
-- Name: objekt pk_objekt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objekt
    ADD CONSTRAINT pk_objekt PRIMARY KEY (id);


--
-- Name: objekt_loc pk_objekt_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objekt_loc
    ADD CONSTRAINT pk_objekt_loc PRIMARY KEY (id);


--
-- Name: obsazeni_objektu pk_obsazeni_objektu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obsazeni_objektu
    ADD CONSTRAINT pk_obsazeni_objektu PRIMARY KEY (id);


--
-- Name: odvod pk_odvod; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.odvod
    ADD CONSTRAINT pk_odvod PRIMARY KEY (cislo);


--
-- Name: omezeni_rezervaci pk_omezeni_rezervaci; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.omezeni_rezervaci
    ADD CONSTRAINT pk_omezeni_rezervaci PRIMARY KEY (objektid);


--
-- Name: osoba pk_osoba; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.osoba
    ADD CONSTRAINT pk_osoba PRIMARY KEY (id);


--
-- Name: zakaznik_changes pk_osoba_changes; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zakaznik_changes
    ADD CONSTRAINT pk_osoba_changes PRIMARY KEY (id);


--
-- Name: otviraci_doba pk_otviraci_doba; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.otviraci_doba
    ADD CONSTRAINT pk_otviraci_doba PRIMARY KEY (objektid, platnostod);


--
-- Name: ovladac_objektu pk_ovladac_objektu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ovladac_objektu
    ADD CONSTRAINT pk_ovladac_objektu PRIMARY KEY (id);


--
-- Name: ovladani_objektu_log pk_ovladani_objektu_log; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ovladani_objektu_log
    ADD CONSTRAINT pk_ovladani_objektu_log PRIMARY KEY (id);


--
-- Name: ovladani_objektu_quido pk_ovladani_objektu_quido; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ovladani_objektu_quido
    ADD CONSTRAINT pk_ovladani_objektu_quido PRIMARY KEY (id);


--
-- Name: ovladani_objektu_server pk_ovladani_objektu_server; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ovladani_objektu_server
    ADD CONSTRAINT pk_ovladani_objektu_server PRIMARY KEY (id);


--
-- Name: ovladani_vstupu pk_ovladani_vstupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ovladani_vstupu
    ADD CONSTRAINT pk_ovladani_vstupu PRIMARY KEY (id);


--
-- Name: ovladani_vstupu_log pk_ovladani_vstupu_log; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ovladani_vstupu_log
    ADD CONSTRAINT pk_ovladani_vstupu_log PRIMARY KEY (id);


--
-- Name: ovladani_vstupu_reader pk_ovladani_vstupu_reader; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ovladani_vstupu_reader
    ADD CONSTRAINT pk_ovladani_vstupu_reader PRIMARY KEY (id);


--
-- Name: permanentka pk_permanentka; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permanentka
    ADD CONSTRAINT pk_permanentka PRIMARY KEY (id);


--
-- Name: permanentka_cinnost pk_permanentka_cinnost; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permanentka_cinnost
    ADD CONSTRAINT pk_permanentka_cinnost PRIMARY KEY (id);


--
-- Name: permanentka_cinnosti pk_permanentka_cinnosti; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permanentka_cinnosti
    ADD CONSTRAINT pk_permanentka_cinnosti PRIMARY KEY (cinnost, permanentka);


--
-- Name: permanentka_zbozi pk_permanentka_zbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permanentka_zbozi
    ADD CONSTRAINT pk_permanentka_zbozi PRIMARY KEY (id);


--
-- Name: permanentka_zbozi_list pk_permanentka_zbozi_list; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permanentka_zbozi_list
    ADD CONSTRAINT pk_permanentka_zbozi_list PRIMARY KEY (permanentka, zbozi);


--
-- Name: platba_poukazkou pk_platba_poukazkou; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.platba_poukazkou
    ADD CONSTRAINT pk_platba_poukazkou PRIMARY KEY (id);


--
-- Name: platba_za_clenstvi pk_platba_za_clenstvi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.platba_za_clenstvi
    ADD CONSTRAINT pk_platba_za_clenstvi PRIMARY KEY (id);


--
-- Name: platba_za_clenstvi_zak pk_platba_za_clenstvi_zak; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.platba_za_clenstvi_zak
    ADD CONSTRAINT pk_platba_za_clenstvi_zak PRIMARY KEY (id);


--
-- Name: podm_vs_nulovy_ucet_pokladny pk_podm_vs_nulovy_ucet_pokladny; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.podm_vs_nulovy_ucet_pokladny
    ADD CONSTRAINT pk_podm_vs_nulovy_ucet_pokladny PRIMARY KEY (podminkavstupu, pokladna);


--
-- Name: podm_vy_nulovy_ucet_pokladny pk_podm_vy_nulovy_ucet_pokladny; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.podm_vy_nulovy_ucet_pokladny
    ADD CONSTRAINT pk_podm_vy_nulovy_ucet_pokladny PRIMARY KEY (podminkavystupu, pokladna);


--
-- Name: podminka_rezervace pk_podminka_rezervace; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.podminka_rezervace
    ADD CONSTRAINT pk_podminka_rezervace PRIMARY KEY (id);


--
-- Name: podminka_vstupu pk_podminka_vstupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.podminka_vstupu
    ADD CONSTRAINT pk_podminka_vstupu PRIMARY KEY (id);


--
-- Name: pohyb_depositu pk_pohyb_depositu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pohyb_depositu
    ADD CONSTRAINT pk_pohyb_depositu PRIMARY KEY (id);


--
-- Name: pohyb_na_pokladne pk_pohyb_na_pokladne; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pohyb_na_pokladne
    ADD CONSTRAINT pk_pohyb_na_pokladne PRIMARY KEY (id);


--
-- Name: pohyb_permanentky pk_pohyb_permanentky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pohyb_permanentky
    ADD CONSTRAINT pk_pohyb_permanentky PRIMARY KEY (id);


--
-- Name: pohyb_zbozi pk_pohyb_zbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pohyb_zbozi
    ADD CONSTRAINT pk_pohyb_zbozi PRIMARY KEY (id);


--
-- Name: pokladna pk_pokladna; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pokladna
    ADD CONSTRAINT pk_pokladna PRIMARY KEY (id);


--
-- Name: pokladna_loc pk_pokladna_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pokladna_loc
    ADD CONSTRAINT pk_pokladna_loc PRIMARY KEY (id);


--
-- Name: polozka_inventury pk_polozka_inventury; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.polozka_inventury
    ADD CONSTRAINT pk_polozka_inventury PRIMARY KEY (id);


--
-- Name: polozka_prijemky pk_polozka_prijemky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.polozka_prijemky
    ADD CONSTRAINT pk_polozka_prijemky PRIMARY KEY (id);


--
-- Name: polozka_slozeneho_zbozi pk_polozka_slozeneho_zbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.polozka_slozeneho_zbozi
    ADD CONSTRAINT pk_polozka_slozeneho_zbozi PRIMARY KEY (id);


--
-- Name: polozka_uctu pk_polozka_uctu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.polozka_uctu
    ADD CONSTRAINT pk_polozka_uctu PRIMARY KEY (id);


--
-- Name: polozka_vydejky pk_polozka_vydejky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.polozka_vydejky
    ADD CONSTRAINT pk_polozka_vydejky PRIMARY KEY (id);


--
-- Name: polozkasazbydph pk_polozkasazbydph; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.polozkasazbydph
    ADD CONSTRAINT pk_polozkasazbydph PRIMARY KEY (id);


--
-- Name: prerusene_clenstvi pk_prerusene_clenstvi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prerusene_clenstvi
    ADD CONSTRAINT pk_prerusene_clenstvi PRIMARY KEY (id);


--
-- Name: prerusene_permanentky pk_prerusene_permanentky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prerusene_permanentky
    ADD CONSTRAINT pk_prerusene_permanentky PRIMARY KEY (id);


--
-- Name: prevodka pk_prevodka; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prevodka
    ADD CONSTRAINT pk_prevodka PRIMARY KEY (id);


--
-- Name: prijemka pk_prijemka; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prijemka
    ADD CONSTRAINT pk_prijemka PRIMARY KEY (id);


--
-- Name: print_template pk_print_template; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.print_template
    ADD CONSTRAINT pk_print_template PRIMARY KEY (id);


--
-- Name: provozovna pk_provozovna; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.provozovna
    ADD CONSTRAINT pk_provozovna PRIMARY KEY (id);


--
-- Name: push_history pk_push_history; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.push_history
    ADD CONSTRAINT pk_push_history PRIMARY KEY (id);


--
-- Name: push_multicast pk_push_multicast; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.push_multicast
    ADD CONSTRAINT pk_push_multicast PRIMARY KEY (id);


--
-- Name: valid_registration_number pk_registration_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.valid_registration_number
    ADD CONSTRAINT pk_registration_number PRIMARY KEY (registrationnumber);


--
-- Name: remote_order_log pk_remote_order_log; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.remote_order_log
    ADD CONSTRAINT pk_remote_order_log PRIMARY KEY (id);


--
-- Name: remote_order_zbozi pk_remote_order_zbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.remote_order_zbozi
    ADD CONSTRAINT pk_remote_order_zbozi PRIMARY KEY (id);


--
-- Name: rychle_volby_historie pk_rychle_volby_historie; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rychle_volby_historie
    ADD CONSTRAINT pk_rychle_volby_historie PRIMARY KEY (id);


--
-- Name: sazbadph pk_sazbadph; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sazbadph
    ADD CONSTRAINT pk_sazbadph PRIMARY KEY (id);


--
-- Name: seq pk_seq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seq
    ADD CONSTRAINT pk_seq PRIMARY KEY (name);


--
-- Name: sklad pk_sklad; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sklad
    ADD CONSTRAINT pk_sklad PRIMARY KEY (id);


--
-- Name: sklad_loc pk_sklad_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sklad_loc
    ADD CONSTRAINT pk_sklad_loc PRIMARY KEY (id);


--
-- Name: sklad_seq pk_sklad_sekvence; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sklad_seq
    ADD CONSTRAINT pk_sklad_sekvence PRIMARY KEY (sklad, sekvence);


--
-- Name: sklad_skupina pk_sklad_skupina; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sklad_skupina
    ADD CONSTRAINT pk_sklad_skupina PRIMARY KEY (sklad, skupina);


--
-- Name: skupina pk_skupina; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.skupina
    ADD CONSTRAINT pk_skupina PRIMARY KEY (id);


--
-- Name: skupina_role pk_skupina_role; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.skupina_role
    ADD CONSTRAINT pk_skupina_role PRIMARY KEY (role, skupina);


--
-- Name: skupina_zakazniku pk_skupina_zakazniku; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.skupina_zakazniku
    ADD CONSTRAINT pk_skupina_zakazniku PRIMARY KEY (id);


--
-- Name: skupina_zakazniku_loc pk_skupina_zakazniku_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.skupina_zakazniku_loc
    ADD CONSTRAINT pk_skupina_zakazniku_loc PRIMARY KEY (id);


--
-- Name: skupina_zakazniku_sazba pk_skupina_zakazniku_sazba; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.skupina_zakazniku_sazba
    ADD CONSTRAINT pk_skupina_zakazniku_sazba PRIMARY KEY (id);


--
-- Name: slevovy_kod_skupiny pk_slevovy_kod_skupiny; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.slevovy_kod_skupiny
    ADD CONSTRAINT pk_slevovy_kod_skupiny PRIMARY KEY (id);


--
-- Name: slevovy_kod_skupiny_zakaznik pk_slevovy_kod_skupiny_zakaznik; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.slevovy_kod_skupiny_zakaznik
    ADD CONSTRAINT pk_slevovy_kod_skupiny_zakaznik PRIMARY KEY (id);


--
-- Name: sms_history pk_sms_history; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_history
    ADD CONSTRAINT pk_sms_history PRIMARY KEY (id);


--
-- Name: splatka_clenstvi pk_splatka_clenstvi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.splatka_clenstvi
    ADD CONSTRAINT pk_splatka_clenstvi PRIMARY KEY (id);


--
-- Name: sport pk_sport; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sport
    ADD CONSTRAINT pk_sport PRIMARY KEY (id);


--
-- Name: sport_instructor pk_sport_instructor; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sport_instructor
    ADD CONSTRAINT pk_sport_instructor PRIMARY KEY (id);


--
-- Name: sport_kategorie pk_sport_kategorie; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sport_kategorie
    ADD CONSTRAINT pk_sport_kategorie PRIMARY KEY (id);


--
-- Name: sport_kategorie_loc pk_sport_kategorie_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sport_kategorie_loc
    ADD CONSTRAINT pk_sport_kategorie_loc PRIMARY KEY (id);


--
-- Name: sport_loc pk_sport_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sport_loc
    ADD CONSTRAINT pk_sport_loc PRIMARY KEY (id);


--
-- Name: statecode pk_statecode; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.statecode
    ADD CONSTRAINT pk_statecode PRIMARY KEY (id);


--
-- Name: stav_skladu pk_stav_skladu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stav_skladu
    ADD CONSTRAINT pk_stav_skladu PRIMARY KEY (id);


--
-- Name: table_category_usergroup pk_table_category_usergroup; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.table_category_usergroup
    ADD CONSTRAINT pk_table_category_usergroup PRIMARY KEY (usergroup_id, category_id);


--
-- Name: template_html_note pk_template_html_note; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.template_html_note
    ADD CONSTRAINT pk_template_html_note PRIMARY KEY (id);


--
-- Name: terminal_payment pk_terminal_payment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.terminal_payment
    ADD CONSTRAINT pk_terminal_payment PRIMARY KEY (id);


--
-- Name: token pk_token; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token
    ADD CONSTRAINT pk_token PRIMARY KEY (id);


--
-- Name: token_confirmation pk_token_confirmation; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token_confirmation
    ADD CONSTRAINT pk_token_confirmation PRIMARY KEY (id);


--
-- Name: touch_category pk_touch_category; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_category
    ADD CONSTRAINT pk_touch_category PRIMARY KEY (id);


--
-- Name: touch_category_usergroup pk_touch_category_usergroup; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_category_usergroup
    ADD CONSTRAINT pk_touch_category_usergroup PRIMARY KEY (usergroup_id, category_id);


--
-- Name: touch_product pk_touch_product; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_product
    ADD CONSTRAINT pk_touch_product PRIMARY KEY (id);


--
-- Name: touch_product_touch_category pk_touch_product_touch_category; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_product_touch_category
    ADD CONSTRAINT pk_touch_product_touch_category PRIMARY KEY (product_id, category_id);


--
-- Name: touch_table pk_touch_table; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_table
    ADD CONSTRAINT pk_touch_table PRIMARY KEY (id);


--
-- Name: touch_table_category pk_touch_table_category; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_table_category
    ADD CONSTRAINT pk_touch_table_category PRIMARY KEY (id);


--
-- Name: touch_table_loc pk_touch_table_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_table_loc
    ADD CONSTRAINT pk_touch_table_loc PRIMARY KEY (id);


--
-- Name: touch_table_touch_table_category pk_touch_table_touch_table_category; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.touch_table_touch_table_category
    ADD CONSTRAINT pk_touch_table_touch_table_category PRIMARY KEY (table_id, category_id);


--
-- Name: typ_depositu pk_typ_depositu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_depositu
    ADD CONSTRAINT pk_typ_depositu PRIMARY KEY (id);


--
-- Name: typ_depositu_loc pk_typ_depositu_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_depositu_loc
    ADD CONSTRAINT pk_typ_depositu_loc PRIMARY KEY (id);


--
-- Name: typ_permanentky pk_typ_permanentky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_permanentky
    ADD CONSTRAINT pk_typ_permanentky PRIMARY KEY (id);


--
-- Name: typ_platebni_karty pk_typ_platebni_karty; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_platebni_karty
    ADD CONSTRAINT pk_typ_platebni_karty PRIMARY KEY (id);


--
-- Name: typ_poukazek pk_typ_poukazek; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_poukazek
    ADD CONSTRAINT pk_typ_poukazek PRIMARY KEY (id);


--
-- Name: typ_tokenu pk_typ_tokenu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_tokenu
    ADD CONSTRAINT pk_typ_tokenu PRIMARY KEY (id);


--
-- Name: typ_tokenu_loc pk_typ_tokenu_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_tokenu_loc
    ADD CONSTRAINT pk_typ_tokenu_loc PRIMARY KEY (id);


--
-- Name: typ_tokenu_podminka_vstupu pk_typ_tokenu_podminka_vstupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_tokenu_podminka_vstupu
    ADD CONSTRAINT pk_typ_tokenu_podminka_vstupu PRIMARY KEY (typtokenu, podminkavstupu);


--
-- Name: typ_tokenu_podminka_vystupu pk_typ_tokenu_podminka_vystupu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typ_tokenu_podminka_vystupu
    ADD CONSTRAINT pk_typ_tokenu_podminka_vystupu PRIMARY KEY (podminkavystupu, typtokenu);


--
-- Name: typdeposituzbozi pk_typdeposituzbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.typdeposituzbozi
    ADD CONSTRAINT pk_typdeposituzbozi PRIMARY KEY (deposit, zbozi);


--
-- Name: ucet pk_ucet; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ucet
    ADD CONSTRAINT pk_ucet PRIMARY KEY (cislo);


--
-- Name: user_qr_access_code pk_user_qr_access_code; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_qr_access_code
    ADD CONSTRAINT pk_user_qr_access_code PRIMARY KEY (id);


--
-- Name: uzaverka_reg_pokladny pk_uzaverka_reg_pokladny; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzaverka_reg_pokladny
    ADD CONSTRAINT pk_uzaverka_reg_pokladny PRIMARY KEY (id);


--
-- Name: uzivatel pk_uzivatel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzivatel
    ADD CONSTRAINT pk_uzivatel PRIMARY KEY (login);


--
-- Name: uzivatel_role pk_uzivatel_role; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzivatel_role
    ADD CONSTRAINT pk_uzivatel_role PRIMARY KEY (id, login);


--
-- Name: uzivatel_session pk_uzivatel_session; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzivatel_session
    ADD CONSTRAINT pk_uzivatel_session PRIMARY KEY (id);


--
-- Name: uzivatel_session_token pk_uzivatel_session_token; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzivatel_session_token
    ADD CONSTRAINT pk_uzivatel_session_token PRIMARY KEY (series);


--
-- Name: video_odkaz pk_video_odkaz; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.video_odkaz
    ADD CONSTRAINT pk_video_odkaz PRIMARY KEY (id);


--
-- Name: vklad pk_vklad; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vklad
    ADD CONSTRAINT pk_vklad PRIMARY KEY (cislo);


--
-- Name: voucher pk_voucher; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voucher
    ADD CONSTRAINT pk_voucher PRIMARY KEY (id);


--
-- Name: vstupni_akce pk_vstupni_akce; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vstupni_akce
    ADD CONSTRAINT pk_vstupni_akce PRIMARY KEY (id);


--
-- Name: vydavac_micku_sluzba pk_vydavac_micku_sluzba; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vydavac_micku_sluzba
    ADD CONSTRAINT pk_vydavac_micku_sluzba PRIMARY KEY (id);


--
-- Name: vydejka pk_vydejka; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vydejka
    ADD CONSTRAINT pk_vydejka PRIMARY KEY (id);


--
-- Name: waiting_storno pk_waiting_storno; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.waiting_storno
    ADD CONSTRAINT pk_waiting_storno PRIMARY KEY (id);


--
-- Name: web_actuality pk_web_actuality; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.web_actuality
    ADD CONSTRAINT pk_web_actuality PRIMARY KEY (id);


--
-- Name: web_informace pk_web_informace; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.web_informace
    ADD CONSTRAINT pk_web_informace PRIMARY KEY (id);


--
-- Name: web_message pk_web_message; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.web_message
    ADD CONSTRAINT pk_web_message PRIMARY KEY (id);


--
-- Name: web_oznameni pk_web_oznameni; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.web_oznameni
    ADD CONSTRAINT pk_web_oznameni PRIMARY KEY (id);


--
-- Name: zakaznik pk_zakaznik; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zakaznik
    ADD CONSTRAINT pk_zakaznik PRIMARY KEY (id);


--
-- Name: zakaznik_skupina_viditelnost pk_zakaznik_skupina_viditelnost; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zakaznik_skupina_viditelnost
    ADD CONSTRAINT pk_zakaznik_skupina_viditelnost PRIMARY KEY (zakaznik, skupina);


--
-- Name: zakaznik_token pk_zakaznik_token; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zakaznik_token
    ADD CONSTRAINT pk_zakaznik_token PRIMARY KEY (id);


--
-- Name: zbozi pk_zbozi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zbozi
    ADD CONSTRAINT pk_zbozi PRIMARY KEY (id);


--
-- Name: zbozi_ean pk_zbozi_ean; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zbozi_ean
    ADD CONSTRAINT pk_zbozi_ean PRIMARY KEY (id);


--
-- Name: zbozi_kategorie pk_zbozi_kategorie; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zbozi_kategorie
    ADD CONSTRAINT pk_zbozi_kategorie PRIMARY KEY (zbozi, kategorie);


--
-- Name: zbozi_loc pk_zbozi_loc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zbozi_loc
    ADD CONSTRAINT pk_zbozi_loc PRIMARY KEY (id);


--
-- Name: prepocet_men prepocet_men_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prepocet_men
    ADD CONSTRAINT prepocet_men_pkey PRIMARY KEY (id);


--
-- Name: push_key push_key_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.push_key
    ADD CONSTRAINT push_key_pkey PRIMARY KEY (id);


--
-- Name: stav_skladu stav_skladu_zbozi_sklad_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stav_skladu
    ADD CONSTRAINT stav_skladu_zbozi_sklad_key UNIQUE (zbozi, sklad);


--
-- Name: ucet ucet_unipayid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ucet
    ADD CONSTRAINT ucet_unipayid_key UNIQUE (unipayid);


--
-- Name: uzivatel uzivatel_aktivacnitoken_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzivatel
    ADD CONSTRAINT uzivatel_aktivacnitoken_key UNIQUE (aktivacnitoken);


--
-- Name: uzivatel_login_attempt uzivatel_login_attempt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzivatel_login_attempt
    ADD CONSTRAINT uzivatel_login_attempt_pkey PRIMARY KEY (id);


--
-- Name: version version_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.version
    ADD CONSTRAINT version_pkey PRIMARY KEY (id);


--
-- Name: zakaznik zakaznik_passwordtoken_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zakaznik
    ADD CONSTRAINT zakaznik_passwordtoken_key UNIQUE (passwordtoken);


--
-- Name: aouth2_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX aouth2_idx ON public.external_client USING btree (oauth2_setting_id);


--
-- Name: areal_loc_areal; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX areal_loc_areal ON public.areal_loc USING btree (areal);


--
-- Name: areal_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX areal_loc_nazev ON public.areal_loc USING btree (jazyk, nazev);


--
-- Name: automatic_message_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX automatic_message_idx ON public.automatic_attachment_template USING btree (automaticmessageid);


--
-- Name: blokace_objektu_loc_blokace; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blokace_objektu_loc_blokace ON public.blokace_objektu_loc USING btree (blokace);


--
-- Name: blokace_objektu_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blokace_objektu_loc_nazev ON public.blokace_objektu_loc USING btree (jazyk, nazev);


--
-- Name: cena_platnost; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX cena_platnost ON public.cena USING btree (platnostod);


--
-- Name: cena_skupina; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX cena_skupina ON public.cena USING btree (skupinaid);


--
-- Name: cena_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX cena_zbozi ON public.cena USING btree (zbozi);


--
-- Name: clen_skupiny_zakazniku_platnost; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX clen_skupiny_zakazniku_platnost ON public.clen_skupiny_zakazniku USING btree (clenstviod, clenstvido);


--
-- Name: clen_skupiny_zakazniku_skupina; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX clen_skupiny_zakazniku_skupina ON public.clen_skupiny_zakazniku USING btree (skupinazakazniku);


--
-- Name: clen_skupiny_zakazniku_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX clen_skupiny_zakazniku_zakaznik ON public.clen_skupiny_zakazniku USING btree (zakaznik);


--
-- Name: clenskupinyzakaznikuid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX clenskupinyzakaznikuid_idx ON public.objednavka_clenstvi USING btree (clenskupinyzakaznikuid);


--
-- Name: consumption_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX consumption_zakaznik ON public.consumption USING btree (zakaznikid);


--
-- Name: deposit_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX deposit_id ON public.deposit USING btree (id);


--
-- Name: deposit_typ; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX deposit_typ ON public.deposit USING btree (typ);


--
-- Name: deposit_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX deposit_zakaznik ON public.deposit USING btree (zakaznik);


--
-- Name: dokladid_pohyb_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX dokladid_pohyb_zbozi ON public.pohyb_zbozi USING btree (dokladid);


--
-- Name: email_history_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX email_history_date ON public.email_history USING btree (date);


--
-- Name: email_omezeni_automatic_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX email_omezeni_automatic_email ON public.message_omezeni USING btree (automatic_message);


--
-- Name: email_queue_idx1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX email_queue_idx1 ON public.email_queue USING btree (id);


--
-- Name: email_queue_idx2; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX email_queue_idx2 ON public.email_queue USING btree (created);


--
-- Name: email_queue_idx3; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX email_queue_idx3 ON public.email_queue USING btree (emailhistory);


--
-- Name: email_queue_idx5; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX email_queue_idx5 ON public.email_queue USING btree (priority DESC, created);


--
-- Name: epatba_datumvzniku; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX epatba_datumvzniku ON public.eplatba USING btree (datumvzniku);


--
-- Name: epatba_typ_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX epatba_typ_status ON public.eplatba USING btree (typ, status);


--
-- Name: epatba_uspesnedokonceno; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX epatba_uspesnedokonceno ON public.eplatba USING btree (uspesnedokonceno);


--
-- Name: epatba_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX epatba_zakaznik ON public.eplatba USING btree (zakaznikid);


--
-- Name: external_entrance_item_entrance; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX external_entrance_item_entrance ON public.external_entrance_item USING btree (external_entrance);


--
-- Name: external_entrance_item_objekt; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX external_entrance_item_objekt ON public.external_entrance_item USING btree (objektid);


--
-- Name: external_entrance_log_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX external_entrance_log_date ON public.external_entrance_log USING btree (requestdate);


--
-- Name: external_entrance_log_entranceitem; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX external_entrance_log_entranceitem ON public.external_entrance_log USING btree (entranceitemid);


--
-- Name: golf_hriste_objekty; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX golf_hriste_objekty ON public.golf_hriste USING btree (objekt1id, objekt2id);


--
-- Name: golf_odpaliste_odpaliste_typ; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX golf_odpaliste_odpaliste_typ ON public.golf_odpaliste USING btree (odpaliste_typ);


--
-- Name: golferid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX golferid_idx ON public.osoba USING btree (golferid);


--
-- Name: historie_nastaveni_cen_vlozeno; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX historie_nastaveni_cen_vlozeno ON public.historie_nastaveni_cen USING btree (vlozenodate);


--
-- Name: historie_slev_skupina_datum; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX historie_slev_skupina_datum ON public.historie_slev USING btree (skupina, datum);


--
-- Name: hromadna_objednavka_objektu_datum; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hromadna_objednavka_objektu_datum ON public.hromadna_objednavka_objektu USING btree (datumvytvoreni, datumstorna);


--
-- Name: hromadna_objednavka_objektu_idx0; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hromadna_objednavka_objektu_idx0 ON public.hromadna_objednavka_objektu USING btree (objektid);


--
-- Name: hromadna_objednavka_objektu_idx1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX hromadna_objednavka_objektu_idx1 ON public.hromadna_objednavka_objektu USING btree (zakaznikid);


--
-- Name: idx_kod; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_kod ON public.mena USING btree (kod);


--
-- Name: idx_kodnum; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_kodnum ON public.mena USING btree (kodnum);


--
-- Name: idx_objednavka_objektu_nadrazena_objednavka_objektu; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_objednavka_objektu_nadrazena_objednavka_objektu ON public.objednavka_objektu USING btree (nadrazena_objednavka_objektu);


--
-- Name: idx_push_history_pushmulticast; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_push_history_pushmulticast ON public.push_history USING btree (pushmulticast);


--
-- Name: idx_push_history_uzivatelid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_push_history_uzivatelid ON public.push_history USING btree (uzivatelid);


--
-- Name: idx_push_multicast_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_push_multicast_date ON public.push_multicast USING btree (date);


--
-- Name: idx_token_confirmation_cas; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_token_confirmation_cas ON public.token_confirmation USING btree (cas);


--
-- Name: inventura_datumzadani; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX inventura_datumzadani ON public.inventura USING btree (datumzadani);


--
-- Name: inventura_sklad; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX inventura_sklad ON public.inventura USING btree (sklad);


--
-- Name: jednotka_loc_jednotka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX jednotka_loc_jednotka ON public.jednotka_loc USING btree (jednotka);


--
-- Name: jednotka_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX jednotka_loc_nazev ON public.jednotka_loc USING btree (jazyk, nazev);


--
-- Name: kalendar_splatek_clenstvi_clen_skupiny_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX kalendar_splatek_clenstvi_clen_skupiny_idx ON public.kalendar_splatek_clenstvi USING btree (clenskupinyid);


--
-- Name: kalendar_splatek_clenstvi_zak_skupina_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX kalendar_splatek_clenstvi_zak_skupina_idx ON public.kalendar_splatek_clenstvi USING btree (skupinaid);


--
-- Name: kalendar_splatek_clenstvi_zakaznik_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX kalendar_splatek_clenstvi_zakaznik_idx ON public.kalendar_splatek_clenstvi USING btree (zakaznikid);


--
-- Name: kategorie_loc_kategorie; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX kategorie_loc_kategorie ON public.kategorie_loc USING btree (kategorie);


--
-- Name: kategorie_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX kategorie_loc_nazev ON public.kategorie_loc USING btree (jazyk, nazev);


--
-- Name: kategorie_nadkategorie; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX kategorie_nadkategorie ON public.kategorie USING btree (nadkategorie);


--
-- Name: member_number_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX member_number_idx ON public.member_number USING btree (number);


--
-- Name: nahradnik_idx1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX nahradnik_idx1 ON public.nahradnik USING btree (objektid);


--
-- Name: nahradnik_idx2; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX nahradnik_idx2 ON public.nahradnik USING btree (sportid);


--
-- Name: nahradnik_idx3; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX nahradnik_idx3 ON public.nahradnik USING btree (arealid);


--
-- Name: nahradnik_idx4; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX nahradnik_idx4 ON public.nahradnik USING btree (zacatek);


--
-- Name: nahradnik_idx5; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX nahradnik_idx5 ON public.nahradnik USING btree (konec);


--
-- Name: nasledne_douctovani_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX nasledne_douctovani_idx ON public.nasledne_douctovani USING btree (zbozi, sklad);


--
-- Name: objednavka_cisloplatby; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_cisloplatby ON public.objednavka USING btree (cisloplatby);


--
-- Name: objednavka_clenstvi_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_clenstvi_objednavka ON public.objednavka_clenstvi USING btree (objednavka);


--
-- Name: objednavka_clenstvi_skupina; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_clenstvi_skupina ON public.objednavka_clenstvi USING btree (skupinaid);


--
-- Name: objednavka_datumzaplaceni; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_datumzaplaceni ON public.objednavka USING btree (datumzaplaceni);


--
-- Name: objednavka_depositu_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_depositu_objednavka ON public.objednavka_depositu USING btree (objednavka);


--
-- Name: objednavka_depositu_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_depositu_zakaznik ON public.objednavka_depositu USING btree (zakaznikid);


--
-- Name: objednavka_ean; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_ean ON public.objednavka USING btree (ean);


--
-- Name: objednavka_idx2; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_idx2 ON public.objednavka USING btree (typplatby);


--
-- Name: objednavka_idx3; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_idx3 ON public.objednavka USING btree (ucetid);


--
-- Name: objednavka_obj_token_token_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_obj_token_token_idx ON public.objednavka_obj_token USING btree (token);


--
-- Name: objednavka_objektu_hromadna; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_objektu_hromadna ON public.objednavka_objektu USING btree (hromadna_objednavka_objektu);


--
-- Name: objednavka_objektu_kod_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_objektu_kod_idx ON public.objednavka_objektu USING btree (kod);


--
-- Name: objednavka_objektu_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_objektu_objednavka ON public.objednavka_objektu USING btree (objednavka);


--
-- Name: objednavka_objektu_sport; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_objektu_sport ON public.objednavka_objektu USING btree (sportid);


--
-- Name: objednavka_objektu_zak_objednavka_objektu; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_objektu_zak_objednavka_objektu ON public.objednavka_objektu_zak USING btree (objednavka_objektu);


--
-- Name: objednavka_objektu_zak_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_objektu_zak_zakaznik ON public.objednavka_objektu_zak USING btree (zakaznikid);


--
-- Name: objednavka_oteviracipokladna; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_oteviracipokladna ON public.objednavka USING btree (oteviracipokladnaid);


--
-- Name: objednavka_permanentky_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_permanentky_objednavka ON public.objednavka_prmanentky USING btree (objednavka);


--
-- Name: objednavka_permanentky_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_permanentky_zakaznik ON public.objednavka_prmanentky USING btree (zakaznikid);


--
-- Name: objednavka_storno_poplatku_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_storno_poplatku_objednavka ON public.objednavka_storno_poplatku USING btree (objednavka);


--
-- Name: objednavka_storno_poplatku_objednavka_objektu_zak; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_storno_poplatku_objednavka_objektu_zak ON public.objednavka_storno_poplatku USING btree (objednavkaobjektuzakid);


--
-- Name: objednavka_storno_poplatku_objekt; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_storno_poplatku_objekt ON public.objednavka_storno_poplatku USING btree (objektid);


--
-- Name: objednavka_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_zakaznik ON public.objednavka USING btree (zakaznikid);


--
-- Name: objednavka_zbozi_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_zbozi_idx ON public.objednavka_zbozi_printed USING btree (objednavkazboziid);


--
-- Name: objednavka_zbozi_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_zbozi_objednavka ON public.objednavka_zbozi USING btree (objednavka);


--
-- Name: objednavka_zbozi_sklad_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objednavka_zbozi_sklad_zbozi ON public.objednavka_zbozi USING btree (zboziid, skladid);


--
-- Name: objekt_areal; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objekt_areal ON public.objekt USING btree (areal);


--
-- Name: objekt_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objekt_loc_nazev ON public.objekt_loc USING btree (jazyk, nazev);


--
-- Name: objekt_loc_objekt; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objekt_loc_objekt ON public.objekt_loc USING btree (objekt);


--
-- Name: objekt_primyvstup; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objekt_primyvstup ON public.objekt USING btree (primyvstup);


--
-- Name: objektid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objektid_idx ON public.ovladac_objektu USING btree (objektid);


--
-- Name: objtok_idx4; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objtok_idx4 ON public.objednavka_token USING btree (tokenid);


--
-- Name: objtok_idx5; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX objtok_idx5 ON public.objednavka_token USING btree (objednavkaid);


--
-- Name: obsazeni_objektu_active; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_active ON public.obsazeni_objektu USING btree (active);


--
-- Name: obsazeni_objektu_hlavni_obsazeni; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_hlavni_obsazeni ON public.obsazeni_objektu USING btree (hlavni_obsazeni);


--
-- Name: obsazeni_objektu_idx0; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_idx0 ON public.obsazeni_objektu USING btree (zacatek);


--
-- Name: obsazeni_objektu_idx1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_idx1 ON public.obsazeni_objektu USING btree (konec);


--
-- Name: obsazeni_objektu_konec; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_konec ON public.obsazeni_objektu USING btree (konec);


--
-- Name: obsazeni_objektu_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_objednavka ON public.obsazeni_objektu USING btree (objednavka_objektu);


--
-- Name: obsazeni_objektu_objekt; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_objekt ON public.obsazeni_objektu USING btree (objektid);


--
-- Name: obsazeni_objektu_otevrene; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_otevrene ON public.obsazeni_objektu USING btree (otevrene);


--
-- Name: obsazeni_objektu_rezervace_in_objekt_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_rezervace_in_objekt_idx ON public.obsazeni_objektu USING btree (objektid, zacatek, konec);


--
-- Name: obsazeni_objektu_zacatek; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX obsazeni_objektu_zacatek ON public.obsazeni_objektu USING btree (zacatek);


--
-- Name: odvod_datumvystaveni; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX odvod_datumvystaveni ON public.odvod USING btree (datumvystaveni);


--
-- Name: osoba_changes_idx2; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX osoba_changes_idx2 ON public.zakaznik_changes USING btree (zakaznikid);


--
-- Name: osoba_cislo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX osoba_cislo ON public.osoba USING btree (cisloprefix, cislosuffix);


--
-- Name: osoba_datumnarozeni; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX osoba_datumnarozeni ON public.osoba USING btree (datumnarozeni);


--
-- Name: osoba_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX osoba_idx ON public.member_number USING btree (osoba_id);


--
-- Name: osoba_idx4; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX osoba_idx4 ON public.osoba USING btree (jmeno);


--
-- Name: osoba_idx5; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX osoba_idx5 ON public.osoba USING btree (prijmeni);


--
-- Name: ovladani_objektu_log_datum; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ovladani_objektu_log_datum ON public.ovladani_objektu_log USING btree (datumspusteni, datumvypnuti);


--
-- Name: ovladani_objektu_log_objekt_cinnost; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ovladani_objektu_log_objekt_cinnost ON public.ovladani_objektu_log USING btree (objektid, cinnostid);


--
-- Name: ovladani_vstupu_log_datum; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ovladani_vstupu_log_datum ON public.ovladani_vstupu_log USING btree (datum);


--
-- Name: ovladani_vstupu_log_idx1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ovladani_vstupu_log_idx1 ON public.ovladani_vstupu_log USING btree (tokenid);


--
-- Name: ovladani_vstupu_log_idx2; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ovladani_vstupu_log_idx2 ON public.ovladani_vstupu_log USING btree (objednavkaobjektuid);


--
-- Name: permanentka_objednavkapermanentky; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX permanentka_objednavkapermanentky ON public.permanentka USING btree (objednavkapermanentkyid);


--
-- Name: permanentka_zakaznik_platnost; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX permanentka_zakaznik_platnost ON public.permanentka USING btree (zakaznikid, platnostod, platnostdo);


--
-- Name: platba_poukazkou_idx0; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX platba_poukazkou_idx0 ON public.platba_poukazkou USING btree (typpoukazky);


--
-- Name: platba_poukazkou_ucet; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX platba_poukazkou_ucet ON public.platba_poukazkou USING btree (ucet);


--
-- Name: platba_za_clenstvi_datumzadani_datumstorna; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX platba_za_clenstvi_datumzadani_datumstorna ON public.platba_za_clenstvi USING btree (datumzadani, datumstorna);


--
-- Name: platba_za_clenstvi_zak_objednavka_clenstvi_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX platba_za_clenstvi_zak_objednavka_clenstvi_idx ON public.platba_za_clenstvi_zak USING btree (objednavkaid);


--
-- Name: pohyb_depositu_cas_deposit_typ; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_depositu_cas_deposit_typ ON public.pohyb_depositu USING btree (cas, deposit, typ);


--
-- Name: pohyb_depositu_deposit; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_depositu_deposit ON public.pohyb_depositu USING btree (deposit);


--
-- Name: pohyb_depositu_doklad; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_depositu_doklad ON public.pohyb_depositu USING btree (dokladid);


--
-- Name: pohyb_na_pokladne_cas; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_na_pokladne_cas ON public.pohyb_na_pokladne USING btree (pokladna, cas);


--
-- Name: pohyb_na_pokladne_doklad; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_na_pokladne_doklad ON public.pohyb_na_pokladne USING btree (pokladna, dokladid);


--
-- Name: pohyb_permanentky_objednavkaobjektu; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_permanentky_objednavkaobjektu ON public.pohyb_permanentky USING btree (objednavkaobjektuid);


--
-- Name: pohyb_permanentky_objednavkazbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_permanentky_objednavkazbozi ON public.pohyb_permanentky USING btree (objednavkazboziid);


--
-- Name: pohyb_permanentky_permanentka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_permanentky_permanentka ON public.pohyb_permanentky USING btree (permanentka);


--
-- Name: pohyb_zbozi_cas; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_zbozi_cas ON public.pohyb_zbozi USING btree (cas);


--
-- Name: pohyb_zbozi_jednotek; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_zbozi_jednotek ON public.pohyb_zbozi USING btree (jednotek);


--
-- Name: pohyb_zbozi_sklad_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pohyb_zbozi_sklad_zbozi ON public.pohyb_zbozi USING btree (zbozi, sklad);


--
-- Name: pokladna_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX pokladna_loc_nazev ON public.pokladna_loc USING btree (jazyk, nazev);


--
-- Name: polozka_inventury_inventura; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_inventury_inventura ON public.polozka_inventury USING btree (inventura);


--
-- Name: polozka_inventury_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_inventury_zbozi ON public.polozka_inventury USING btree (zboziid);


--
-- Name: polozka_prijemky_pohyb_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_prijemky_pohyb_zbozi ON public.pohyb_zbozi USING btree (polozka_prijemky);


--
-- Name: polozka_prijemky_prijemka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_prijemky_prijemka ON public.polozka_prijemky USING btree (prijemka);


--
-- Name: polozka_prijemky_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_prijemky_zbozi ON public.polozka_prijemky USING btree (zbozi);


--
-- Name: polozka_slozeneho_zbozi_slozene_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_slozeneho_zbozi_slozene_zbozi ON public.polozka_slozeneho_zbozi USING btree (slozene_zbozi);


--
-- Name: polozka_uctu_ucet; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_uctu_ucet ON public.polozka_uctu USING btree (ucet);


--
-- Name: polozka_uctu_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_uctu_zbozi ON public.polozka_uctu USING btree (zbozi, sklad);


--
-- Name: polozka_vydejky_pohyb_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_vydejky_pohyb_zbozi ON public.pohyb_zbozi USING btree (polozka_vydejky);


--
-- Name: polozka_vydejky_vydejka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_vydejky_vydejka ON public.polozka_vydejky USING btree (vydejka);


--
-- Name: polozka_vydejky_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX polozka_vydejky_zbozi ON public.polozka_vydejky USING btree (zbozi);


--
-- Name: poznamka_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX poznamka_zakaznik ON public.html_note USING btree (zakaznikid);


--
-- Name: prepocet_men_mena; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prepocet_men_mena ON public.prepocet_men USING btree (mena);


--
-- Name: prepocet_men_platnost; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prepocet_men_platnost ON public.prepocet_men USING btree (platnostod, platnostdo);


--
-- Name: prerusene_clenstvi_zakaznikid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prerusene_clenstvi_zakaznikid ON public.prerusene_clenstvi USING btree (zakaznikid, sequence);


--
-- Name: prevodka_caszadani_casstorna; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prevodka_caszadani_casstorna ON public.prevodka USING btree (caszadani, casstorna);


--
-- Name: prevodka_prijemka_vydejka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prevodka_prijemka_vydejka ON public.prevodka USING btree (prijemka, vydejka);


--
-- Name: prijemka_casnaskladneni_sklad_odberatel; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX prijemka_casnaskladneni_sklad_odberatel ON public.prijemka USING btree (casnaskladneni, sklad, dodavatel);


--
-- Name: remote_order_log_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX remote_order_log_date ON public.remote_order_log USING btree (date);


--
-- Name: remote_order_zbozi_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX remote_order_zbozi_zbozi ON public.remote_order_zbozi USING btree (zboziid);


--
-- Name: seq_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX seq_type ON public.seq USING btree (type);


--
-- Name: sklad_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sklad_loc_nazev ON public.sklad_loc USING btree (jazyk, nazev);


--
-- Name: sklad_loc_sklad; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sklad_loc_sklad ON public.sklad_loc USING btree (sklad);


--
-- Name: skupina_zakazniku_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX skupina_zakazniku_loc_nazev ON public.skupina_zakazniku_loc USING btree (jazyk, nazev);


--
-- Name: skupina_zakazniku_loc_skupina; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX skupina_zakazniku_loc_skupina ON public.skupina_zakazniku_loc USING btree (skupina);


--
-- Name: splatka_clenstvi_kalendar_splatek_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX splatka_clenstvi_kalendar_splatek_idx ON public.splatka_clenstvi USING btree (kalendar_splatek);


--
-- Name: splatka_clenstvi_objednavka_clenstvi_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX splatka_clenstvi_objednavka_clenstvi_idx ON public.splatka_clenstvi USING btree (objednavkaclenstviid);


--
-- Name: splatka_clenstvi_platba_za_clenstvi_zak_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX splatka_clenstvi_platba_za_clenstvi_zak_idx ON public.splatka_clenstvi USING btree (platbazaclenstvizakid);


--
-- Name: sport_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sport_loc_nazev ON public.sport_loc USING btree (jazyk, nazev);


--
-- Name: sport_loc_sport; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sport_loc_sport ON public.sport_loc USING btree (sport);


--
-- Name: sport_nadrazeny_sport; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sport_nadrazeny_sport ON public.sport USING btree (nadrazeny_sport);


--
-- Name: sport_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sport_zbozi ON public.sport USING btree (zboziid);


--
-- Name: stav_skladu_mnozstvi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX stav_skladu_mnozstvi ON public.stav_skladu USING btree (mnozstvi, minimalnimnozstvi);


--
-- Name: stav_skladu_zbozi_sklad; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX stav_skladu_zbozi_sklad ON public.stav_skladu USING btree (zbozi, sklad);


--
-- Name: token_typ; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX token_typ ON public.token USING btree (typ);


--
-- Name: typ_depositu_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX typ_depositu_loc_nazev ON public.typ_depositu_loc USING btree (jazyk, nazev);


--
-- Name: typ_depositu_loc_typ_depositu; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX typ_depositu_loc_typ_depositu ON public.typ_depositu_loc USING btree (typ_depositu);


--
-- Name: typ_permanentky_viditelny; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX typ_permanentky_viditelny ON public.typ_permanentky USING btree (viditelny);


--
-- Name: typ_tokenu_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX typ_tokenu_loc_nazev ON public.typ_tokenu_loc USING btree (jazyk, nazev);


--
-- Name: typ_tokenu_loc_typ_tokenu; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX typ_tokenu_loc_typ_tokenu ON public.typ_tokenu_loc USING btree (typ_tokenu);


--
-- Name: ucet_cisloplatby; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ucet_cisloplatby ON public.ucet USING btree (cisloplatby);


--
-- Name: ucet_datumvystaveni_storna; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ucet_datumvystaveni_storna ON public.ucet USING btree (datumvystaveni, datumstorna);


--
-- Name: ucet_pokladna; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ucet_pokladna ON public.ucet USING btree (pokladna);


--
-- Name: ucet_typkarty; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ucet_typkarty ON public.ucet USING btree (typplatebnikartyid);


--
-- Name: ucet_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ucet_zakaznik ON public.ucet USING btree (zakaznikid);


--
-- Name: uplatneni_slevoveho_kodu_objednavka; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX uplatneni_slevoveho_kodu_objednavka ON public.slevovy_kod_skupiny_zakaznik USING btree (objednavkaid);


--
-- Name: uplatneni_slevoveho_kodu_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX uplatneni_slevoveho_kodu_zakaznik ON public.slevovy_kod_skupiny_zakaznik USING btree (zakaznikid);


--
-- Name: uzivatel_login_attempt_ip_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX uzivatel_login_attempt_ip_index ON public.uzivatel_login_attempt USING btree (ip);


--
-- Name: uzivatel_login_attempt_login_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX uzivatel_login_attempt_login_index ON public.uzivatel_login_attempt USING btree (login);


--
-- Name: uzivatel_skupina; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX uzivatel_skupina ON public.uzivatel USING btree (skupina);


--
-- Name: vklad_datumvystaveni_pokladna; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX vklad_datumvystaveni_pokladna ON public.vklad USING btree (datumvystaveni, pokladna);


--
-- Name: voucher_objednavkaid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX voucher_objednavkaid ON public.voucher USING btree (objednavkaid);


--
-- Name: vstupni_akce_objektid_sportid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX vstupni_akce_objektid_sportid ON public.vstupni_akce USING btree (objektid, sportid);


--
-- Name: vydavac_micku_sluzba_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX vydavac_micku_sluzba_zbozi ON public.vydavac_micku_sluzba USING btree (zboziid);


--
-- Name: vydejka_casvyskladneni_sklad_odberatel; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX vydejka_casvyskladneni_sklad_odberatel ON public.vydejka USING btree (casvyskladneni, sklad, odberatelid);


--
-- Name: waiting_storno_idx1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX waiting_storno_idx1 ON public.waiting_storno USING btree (objektid);


--
-- Name: waiting_storno_idx2; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX waiting_storno_idx2 ON public.waiting_storno USING btree (zacatek);


--
-- Name: waiting_storno_idx3; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX waiting_storno_idx3 ON public.waiting_storno USING btree (konec);


--
-- Name: zakaznik_datumposlednizmeny; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zakaznik_datumposlednizmeny ON public.zakaznik USING btree (datumposlednizmeny);


--
-- Name: zakaznik_osoba; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zakaznik_osoba ON public.zakaznik USING btree (osoba);


--
-- Name: zakaznik_token_token; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zakaznik_token_token ON public.zakaznik_token USING btree (token, casdo);


--
-- Name: zakaznik_token_zakaznik; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zakaznik_token_zakaznik ON public.zakaznik_token USING btree (zakaznik, token, casod, casdo);


--
-- Name: zakaznik_uzivatel; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zakaznik_uzivatel ON public.zakaznik USING btree (uzivatel);


--
-- Name: zakaznik_viditelny; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zakaznik_viditelny ON public.zakaznik USING btree (viditelny);


--
-- Name: zbozi_loc_nazev; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zbozi_loc_nazev ON public.zbozi_loc USING btree (jazyk, nazev);


--
-- Name: zbozi_loc_zbozi; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zbozi_loc_zbozi ON public.zbozi_loc USING btree (zbozi);


--
-- Name: zbozi_typ; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX zbozi_typ ON public.zbozi USING btree (typ);


--
-- Name: cena cena_sazbadphid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cena
    ADD CONSTRAINT cena_sazbadphid_fkey FOREIGN KEY (sazbadphid) REFERENCES public.sazbadph(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: instructor_activity fk_instructor_activity_activities; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.instructor_activity
    ADD CONSTRAINT fk_instructor_activity_activities FOREIGN KEY (instructor_id) REFERENCES public.instructor(id);


--
-- Name: instructor_activity fk_instructor_activity_instructors; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.instructor_activity
    ADD CONSTRAINT fk_instructor_activity_instructors FOREIGN KEY (activity_id) REFERENCES public.activity(id);


--
-- Name: nadobjekt_objekt fk_nadobjekt_objekt_nadobjekty; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nadobjekt_objekt
    ADD CONSTRAINT fk_nadobjekt_objekt_nadobjekty FOREIGN KEY (objekt) REFERENCES public.objekt(id);


--
-- Name: nadobjekt_objekt fk_nadobjekt_objekt_podobjekty; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nadobjekt_objekt
    ADD CONSTRAINT fk_nadobjekt_objekt_podobjekty FOREIGN KEY (nadobjekt) REFERENCES public.objekt(id);


--
-- Name: podminka_vstupu fk_podminka_vstupu_casoveomezenivv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.podminka_vstupu
    ADD CONSTRAINT fk_podminka_vstupu_casoveomezenivv FOREIGN KEY (casoveomezeni) REFERENCES public.casove_omezeni_vv(id);


--
-- Name: sport_instructor fk_sport_instructor_instructor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sport_instructor
    ADD CONSTRAINT fk_sport_instructor_instructor FOREIGN KEY (instructor_id) REFERENCES public.instructor(id);


--
-- Name: sport_instructor fk_sport_instructor_sport; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sport_instructor
    ADD CONSTRAINT fk_sport_instructor_sport FOREIGN KEY (sport_id) REFERENCES public.sport(id);


--
-- Name: uzaverka_reg_pokladny fk_uzaverka_reg_pokladny_pokladna; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzaverka_reg_pokladny
    ADD CONSTRAINT fk_uzaverka_reg_pokladny_pokladna FOREIGN KEY (pokladna) REFERENCES public.pokladna(id);


--
-- Name: polozkasazbydph polozkasazbydph_sazbadph_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.polozkasazbydph
    ADD CONSTRAINT polozkasazbydph_sazbadph_fkey FOREIGN KEY (sazbadphid) REFERENCES public.sazbadph(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

