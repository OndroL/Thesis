package cz.inspire.enterprise.module.common.data;

import java.io.Serializable;
import java.util.List;
/**
 * Clubspire, (c) Inspire CZ 2004-2013
 * 
 * @author Marek
 */
public class NastaveniViditelnostiPlatebDO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int typPlatby;
    
    private List<String> skupinyList;

    public NastaveniViditelnostiPlatebDO(int typPlatby, List<String> skupinyList) {
        this.typPlatby = typPlatby;
        this.skupinyList = skupinyList;
    }

    public int getTypPlatby() {
        return typPlatby;
    }

    public void setTypPlatby(int typPlatby) {
        this.typPlatby = typPlatby;
    }

    public List<String> getSkupinyList() {
        return skupinyList;
    }

    public void setSkupinyList(List<String> skupinyList) {
        this.skupinyList = skupinyList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NastaveniViditelnostiPlatebDO other = (NastaveniViditelnostiPlatebDO) obj;
        if (this.typPlatby != other.typPlatby) {
            return false;
        }
        if (this.skupinyList != other.skupinyList && (this.skupinyList == null || !this.skupinyList.equals(other.skupinyList))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
