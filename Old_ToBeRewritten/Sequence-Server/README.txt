findByPokladnaType může jít pryč, nikde se nevolá

sklad_seq je vazba napsaná v rámci SkladBean na entitu SequenceBean, musí se s tím počítat a až ti dáme Sklad-Server, tak pak se do této entity beany se musí přidat ten finder findBySkladType. 
Teď to asi nepůjde vymyslet aby tam byl (a dělal to co má) - nebo možná přes @Query to půjde, jen nebude funkční? 

Jsou tady nějaké metody jako např. next nebo preInit, které budeme muset zachovat a někam výše přesunout. Na tobě je jen nás o tom informovat.

+ pořešit ejbPostCreate