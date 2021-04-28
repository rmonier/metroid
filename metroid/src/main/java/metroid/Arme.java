/* FICHIER ARME.JAVA :
 *      - ARME
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
import sdljava.audio.*;
import sdljava.mixer.*;

/** Arme
 * @author Romain MONIER, Quentin KAIFFER
 */
public class Arme
{
    /*  ATTRIBUTS   */
    
    private String m_nom;
    private int m_degats;
    private int m_munitions, m_vitesse_mun;
    private int m_munitions_max;
    private DirProjectile m_direction;
    private SDLSurface m_sprites[];
    private MixChunk m_son_tir_projectile, m_son_explosion_projectile;
    private boolean m_lancement, m_gravite;
    
	/**
	 * Constructeur
     * @author Romain MONIER, Quentin KAIFFER
     * @param nom Le nom de l'arme
     * @param degats Les dégats infligés par le projectile tiré
     * @param munitions Nombre de munitions max
     * @param vitesse_mun Vitesse des munitions
     * @param gravite Sensible à la gravité
     * @param son_tir_projectile Son du tir du projectile
     * @param son_explosion_projectile Son de l'explosion du projectile
     * @param sprites Tableau qui contient les sprites des projectiles
	*/
    public Arme(String nom, int degats, int munitions, int vitesse_mun, boolean gravite, MixChunk son_tir_projectile, MixChunk son_explosion_projectile, SDLSurface sprites[])
    {
        m_nom = nom;
        m_vitesse_mun = vitesse_mun;
        m_direction = DirProjectile.DROITE;
        m_degats = degats;
        m_munitions = munitions;
        m_munitions_max = m_munitions;
        m_gravite = gravite;
        
        m_sprites = new SDLSurface[sprites.length];
        for(int i = 0 ; i < sprites.length ; i++)
            m_sprites[i] = sprites[i];
        
        m_son_tir_projectile = son_tir_projectile;
        m_son_explosion_projectile = son_explosion_projectile;
        m_lancement = false;
    }    
    
    /*  METHODES    */
    
    /**
     * Libère de la mémoire les sprites du projectile
     * @author Romain MONIER
     * @return Vrai si l'opération s'est bien passée (non implémenté)
     */
    public boolean destroy() throws SDLException, InterruptedException
    {
        for(int i = 0 ; i < m_sprites.length ; i++)
            m_sprites[i].freeSurface();
            
        SDLMixer.freeChunk(m_son_tir_projectile);
        SDLMixer.freeChunk(m_son_explosion_projectile);
            
        return true;
    }
    
    /**
     * Tirer
     * @author Romain MONIER, Quentin KAIFFER
     * @param direction La direction du tir
     * @return L'objet Projectile qui vient d'être tiré
     */
    public Projectile tirer(DirProjectile direction)
    {
        m_lancement = true;
        return new Projectile(m_degats, direction, m_vitesse_mun, m_gravite, m_son_tir_projectile, m_son_explosion_projectile, m_sprites);
    }
    
    /*  SETTER  */
    
    /**
     * Modifier les munitions restantes
     * @author Romain MONIER, Quentin KAIFFER
     * @param munitions Le nouveau nombre de munitions
     */
    public void setMunitions(int munitions)
    {
        if(munitions >= 0 && munitions <= m_munitions_max)
            m_munitions = munitions;
    }
    
    /**
     * Indique que le projectile a déjà été lancé
     * @author Romain MONIER
     */
    public void setLance()
    {
        m_lancement = false;
    }
    
    /*  GETTERS */
    
    /**
     * Retourne si le projectile vient d'être lancé
     * @author Romain MONIER
     * @return Vrai si lancé à l'instant
     */
    public boolean isLance()
    {
        return m_lancement;
    }
    
    /**
     * Retourne les munitions restantes
     * @author Romain MONIER, Quentin KAIFFER
     * @return Nombre de munitions restantes
     */
    public int getMunitions()
    {
        return m_munitions;
    }
    
    /**
     * Retourne les munitions max
     * @author Romain MONIER, Quentin KAIFFER
     * @return Nombre de munitions max
     */
    public int getMunitionsMax()
    {
        return m_munitions_max;
    }
    
    /**
     * Retourne le nom de l'arme
     * @author Romain MONIER, Quentin KAIFFER
     * @return Le nom
     */
    public String getNom()
    {
        return m_nom;
    }
    
    /**
     * Retourne les sprites des projectiles de l'arme
     * @author Romain MONIER, Quentin KAIFFER
     * @return Un tableau de sprites (surfaces)
     */
    public SDLSurface[] getSprites()
    {
        return m_sprites;
    }
    
    /**
     * Retourne le sprite actuel des projectiles de l'arme
     * @author Romain MONIER
     * @return Le sprite dirigé (surface)
     */
    public SDLSurface getSprite()
    {
        return m_sprites[m_direction.ordinal()];
    }
    
    /**
     * Retourne le son du tir du projectile
     * @author Romain MONIER
     * @return Le son (MixChunk)
     */
    public MixChunk getSonTir()
    {
        return m_son_tir_projectile;
    }
    
    /**
     * Retourne le son de l'explosion du projectile
     * @author Romain MONIER
     * @return Le son (MixChunk)
     */
    public MixChunk getSonExplosion()
    {
        return m_son_explosion_projectile;
    }
    
    /**
     * Retourne les dégats de l'explosion du projectile
     * @author Romain MONIER
     * @return Les dégats
     */
    public int getDegats()
    {
        return m_degats;
    }
}
