/* FICHIER PERSONNAGE.JAVA :
 *      - PERSONNAGES (SAMUS & ENNEMIS)
 * 
 *  DERNIÈRE MÀJ : 31/05/2017 par RM
 *  CRÉÉ PAR ROMAIN MONIER & QUENTIN KAIFFER
 *  2016/2017
 * ------------------------------------------
 *  INFOS :
 *      - 
 * ------------------------------------------
 */

package metroid;
 
import sdljava.video.SDLSurface;
import sdljava.video.SDLRect;
import sdljava.SDLException;

/** Personnage
 * @author Romain MONIER, Quentin KAIFFER
 */
public class Personnage
{
    /*  ATTRIBUTS   */
    
    private String m_nom;
    private int m_pv;
    private int m_pv_max;
    private int m_position_x;
    private int m_position_y;
    private SDLSurface m_sprites[];
    private Arme m_arme_principale;
    private Arme m_arme_secondaire;
    private boolean m_arme_unique;
    private DirSprite m_direction;
    private int m_vitesse;
    private int m_niveau_saut_max;
    private int m_niveau_saut;
    private int m_vitesse_saut;
    private boolean m_saut;
    
	/**
	 * Constructeur avec arme unique
     * @author Romain MONIER, Quentin KAIFFER
     * @param nom Le nom du personnage
     * @param pv Son nombre de points de vie
     * @param vitesse Vitesse du personnage
     * @param vitesse_saut Vitesse de saut
     * @param niveau_saut_max Niveau de saut maximum
     * @param sprites Tableau qui contient ses sprites
     * @param arme_principale Arme principale
	*/
    public Personnage(String nom, int pv, int vitesse, int vitesse_saut, int niveau_saut_max, SDLSurface sprites[], Arme arme_principale)
    {
        m_arme_unique = true;
        m_vitesse = vitesse;
        m_direction = DirSprite.DEVANT;
        m_nom = nom;
        m_pv = pv;
        m_pv_max = m_pv;
        m_sprites = new SDLSurface[sprites.length];
        for(int i = 0 ; i < sprites.length ; i++)
            m_sprites[i] = sprites[i];
        m_arme_principale = arme_principale;
        
        m_position_x = 0;
        m_position_y = 0;
        
        m_niveau_saut_max = niveau_saut_max;
        m_niveau_saut = 0;
        m_vitesse_saut = vitesse_saut;
        m_saut = false;
    }
    
	/**
	 * Constructeur avec arme secondaire
     * @author Romain MONIER
     * @param nom Le nom du personnage
     * @param pv Son nombre de points de vie
     * @param vitesse Vitesse du personnage
     * @param vitesse_saut Vitesse de saut
     * @param niveau_saut_max Niveau de saut maximum
     * @param sprites Tableau qui contient ses sprites
     * @param arme_principale Arme principale
     * @param arme_secondaire Arme secondaire
	*/
    public Personnage(String nom, int pv, int vitesse, int vitesse_saut, int niveau_saut_max, SDLSurface sprites[], Arme arme_principale, Arme arme_secondaire)
    {
        m_arme_unique = false;
        m_vitesse = vitesse;
        m_direction = DirSprite.DEVANT;
        m_nom = nom;
        m_pv = pv;
        m_pv_max = m_pv;
        m_sprites = new SDLSurface[sprites.length];
        for(int i = 0 ; i < sprites.length ; i++)
            m_sprites[i] = sprites[i];
        m_arme_principale = arme_principale;
        m_arme_secondaire = arme_secondaire;
        
        m_position_x = 0;
        m_position_y = 0;
        
        m_niveau_saut_max = niveau_saut_max;
        m_niveau_saut = 0;
        m_vitesse_saut = vitesse_saut;
        m_saut = false;
    }
    
    /*  METHODES    */
    
    /**
     * Libère de la mémoire les sprites du personnage et ceux de son arme/projectiles
     * @author Romain MONIER
     * @return Vrai si l'opération s'est bien passée (non implémenté)
     */
    public boolean destroy() throws SDLException, InterruptedException
    {
        for(int i = 0 ; i < m_sprites.length ; i++)
            m_sprites[i].freeSurface();
        m_arme_principale.destroy();
        if(!m_arme_unique)
            m_arme_secondaire.destroy();
            
        return true;
    }
    
    /**
     * S'infliger des dégats
     * @author Romain MONIER, Quentin KAIFFER
     * @param degats Les dégats à s'infliger
     */
    public void recevoir_degats(int degats)
    {
        m_pv -= degats;
        if(m_pv <= 0)
            m_pv = 0;
    }
    
    /**
     * Vérification de la possibilité de tirer avec l'arme principale et utilisation de munition si nécéssaire
     * @author Romain MONIER, Quentin KAIFFER
     * @return Vrai si tir possible
     */
    public boolean tirerArmePrincipale()
    {
        if(m_arme_principale.getMunitions() > 0 || m_arme_principale.getMunitions() == -1){
            if(m_arme_principale.getMunitions() != -1)
                m_arme_principale.setMunitions(m_arme_principale.getMunitions() - 1);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Vérification de la possibilité de tirer avec l'arme secondaire et utilisation de munition si nécéssaire
     * @author Romain MONIER, Quentin KAIFFER
     * @return Vrai si tir possible
     */
    public boolean tirerArmeSecondaire()
    {
        if(!m_arme_unique && (m_arme_secondaire.getMunitions() > 0 || m_arme_secondaire.getMunitions() == -1)){
            if(m_arme_secondaire.getMunitions() != -1)
                m_arme_secondaire.setMunitions(m_arme_secondaire.getMunitions() - 1);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Récupérer de la vie
     * @author Romain MONIER, Quentin KAIFFER
     * @param vie Les PV à restaurer
     */
    public void recevoir_vie(int vie)
    {
        m_pv += vie;
        if(m_pv > m_pv_max)
            m_pv = m_pv_max;
    }
    
    /**
     * Vérification de l'état du personnage
     * @author Romain MONIER, Quentin KAIFFER
     * @return Vrai si vivant
     */
    public boolean isAlive()
    {
        if(m_pv <= 0)
            return false;
        
        return true;
    }
    
    /**
     * Augmentation de la position du saut
     * @author Quentin KAIFFER
     */
    public void sauter()
    {
		m_niveau_saut += m_vitesse_saut;
		
	}
    
    /*  GETTERS */
    
    /**
     * Retourne les PV restants
     * @author Romain MONIER, Quentin KAIFFER
     * @return Les PV actuels
     */
    public int getPV()
    {
        return m_pv;
    }
    
    /**
     * Retourne les PV max
     * @author Romain MONIER, Quentin KAIFFER
     * @return Les PV maxima
     */
    public int getPVMax()
    {
        return m_pv_max;
    }
    
    /**
     * Retourne la position actuelle (SDLRect)
     * @author Romain MONIER
     * @return La position
     */
    public SDLRect getPosition()
    {
        return new SDLRect(m_position_x, m_position_y);
    }
    
    /**
     * Retourne la coordonée X de la position actuelle
     * @author Romain MONIER
     * @return La position X
     */
    public int getPositionX()
    {
        return m_position_x;
    }
    
    /**
     * Retourne la coordonée Y de la position actuelle
     * @author Romain MONIER
     * @return La position Y
     */
    public int getPositionY()
    {
        return m_position_y;
    }
    
    /**
     * Retourne le nom du personnage
     * @author Romain MONIER, Quentin KAIFFER
     * @return Le nom
     */
    public String getNom()
    {
        return m_nom;
    }
    
    /**
     * Retourne les sprites du personnage
     * @author Romain MONIER
     * @return Un tableau de sprites (surfaces)
     */
    public SDLSurface[] getSprites()
    {
        return m_sprites;
    }
    
    /**
     * Retourne le sprite actuel du personnage
     * @author Romain MONIER
     * @return Le sprite dirigé (surface)
     */
    public SDLSurface getSprite()
    {
        return m_sprites[m_direction.ordinal()];
    }
    
    /**
     * Retourne la direction du personnage
     * @author Romain MONIER
     * @return La direction
     */
    public DirSprite getDirection()
    {
        return m_direction;
    }
    
    /**
     * Retourne la vitesse du personnage
     * @author Romain MONIER
     * @return La vitesse (nombre de pixels par boucle)
     */
    public int getVitesse()
    {
        return m_vitesse;
    }
    
    /**
     * Retourne l'arme principale
     * @author Romain MONIER
     * @return L'arme principale
     */
    public Arme getArmePrincipale()
    {
        return m_arme_principale;
    }
    
    /**
     * Retourne l'arme secondaire et le cas échéant la principale
     * @author Romain MONIER
     * @return L'arme secondaire ou la principale si la secondaire n'existe pas
     */
    public Arme getArmeSecondaire()
    {
        if(!m_arme_unique)
            return m_arme_secondaire;
            
        return m_arme_principale;
    }
    
    /**
     * Retourne la hauteur maximale de saut
     * @author Quentin KAIFFER
     * @return La hauteur de saut maximale
     */
    public int getNiveauSautMax()
    {
		return m_niveau_saut_max;
	}
	
    /**
     * Retourne la hauteur restante de saut
     * @author Quentin KAIFFER
     * @return La hauteur restante de saut
     */
    public int getNiveauSaut()
    {
		return m_niveau_saut;
	}
	
    /**
     * Retourne la vitesse du saut
     * @author Quentin KAIFFER
     * @return La vitesse du saut
     */
    public int getVitesseSaut()
    {
		return m_vitesse_saut;
	}
	
    /**
     * Retourne le boolean du statut saut
     * @author Quentin KAIFFER
     * @return le boolean du statut saut
     */
    public boolean getSaut()
    {
		return m_saut;
	}
	
    /**
     * Retourne si le personnage a une arme unique ou non
     * @author Romain MONIER
     * @return Vrai si arme unique
     */
    public boolean getArmeUnique()
    {
		return m_arme_unique;
	}
    
    
    /*  SETTERS */
    
    /**
     * Modifie la coordonnée X de la position
     * @author Romain MONIER
     * @param position_x La nouvelle position
     */
    public void setPositionX(int position_x)
    {
        m_position_x = position_x;
    }
    
    /**
     * Modifie la coordonnée Y de la position
     * @author Romain MONIER
     * @param position_y La nouvelle position
     */
    public void setPositionY(int position_y)
    {
        m_position_y = position_y;
    }
    
    /**
     * Modifie la direction du personnage
     * @author Romain MONIER
     * @param direction La nouvelle direction
     */
    public void setDirection(DirSprite direction)
    {
        m_direction = direction;
    }
    
    /**
     * Modifie la vitesse du personnage
     * @author Romain MONIER
     * @param vitesse La nouvelle vitesse
     */
    public void setVitesse(int vitesse)
    {
        m_vitesse = vitesse;
    }
    
    /**
     * Modifie la hauteur de saut restante
     * @author Quentin KAIFFER
     * @param restant la valeur restante pouvant être sautée
     */
    public void setNiveauSaut(int restant)
    {
		m_niveau_saut = restant;
	}
	
	/**
     * Modifie le boolean saut du personnage
     * @author Quentin KAIFFER
     * @param saut le boolean du nouveau saut
     */
	 public void setSaut(boolean saut)
    {
		m_saut = saut;
	}

}
