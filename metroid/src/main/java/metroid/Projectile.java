/* FICHIER PROJECTILE.JAVA :
 *      - PROJECTILE
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

/** Projectile
 * @author Romain MONIER, Quentin KAIFFER
 */
public class Projectile
{
    /*  ATTRIBUTS   */
    
    private boolean m_onscreen, m_explose, m_gravite;
    private int m_degats, m_vitesse;
    private DirProjectile m_direction;
    private int m_position_x;
    private int m_position_y;
    private SDLSurface m_sprites[];
    private MixChunk m_son_tir, m_son_explosion;
    
	/**
	 * Constructeur
     * @author Romain MONIER, Quentin KAIFFER
     * @param degats Les dégats infligés
     * @param direction La direction du projectile
     * @param vitesse Vitesse des munitions
     * @param gravite Sensible à la gravité
     * @param son_tir Son du tir du projectile
     * @param son_explosion Son de l'explosion du projectile
     * @param sprites Tableau qui contient les sprites des projectiles
	*/
    public Projectile(int degats, DirProjectile direction, int vitesse, boolean gravite, MixChunk son_tir, MixChunk son_explosion, SDLSurface sprites[])
    {
        m_direction = direction;
        m_onscreen = true;
        m_explose = false;
        m_degats = degats;
        m_vitesse = vitesse;
        m_gravite = gravite;
        
        m_sprites = new SDLSurface[sprites.length];
        for(int i = 0 ; i < sprites.length ; i++)
            m_sprites[i] = sprites[i];
            
        m_position_x = 0;
        m_position_y = 0;
        
        m_son_tir = son_tir;
        m_son_explosion = son_explosion;
    }
    
    /*  METHODES    */
    
    /**
     * Explosion du projectile sur un Personnage (inflige des dégats et détruit le projectile)
     * @author Romain MONIER, Quentin KAIFFER
     * @param victime Le personnage touché
     */
    public void explosion(Personnage victime) throws SDLException, InterruptedException
    {
        victime.recevoir_degats(m_degats);
        m_position_y -= 10; // pour que l'explosion soit au niveau du contact
        m_direction = DirProjectile.EXPLOSION;
        m_explose = true;
    }
    
    /*  GETTERS */
    
    /**
     * Vérifie si le Projectile est à l'écran (sinon il est hors terrain ou détruit)
     * @author Romain MONIER
     * @return Vrai si à l'écran
     */
    public boolean isOnScreen()
    {
        return m_onscreen;
    }
    
    /**
     * Retourne si le projectile vient d'exploser
     * @author Romain MONIER, Quentin KAIFFER
     * @return Vrai si explosé à l'instant
     */
    public boolean isExploding()
    {
        return m_explose;
    }
    
    /**
     * Retourne la vitesse du projectile
     * @author Romain MONIER, Quentin KAIFFER
     * @return La vitesse (nombre de pixels par boucle)
     */
    public int getVitesse()
    {
        return m_vitesse;
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
     * @author Romain MONIER, Quentin KAIFFER
     * @return La position X
     */
    public int getPositionX()
    {
        return m_position_x;
    }
    
    /**
     * Retourne la coordonée Y de la position actuelle
     * @author Romain MONIER, Quentin KAIFFER
     * @return La position Y
     */
    public int getPositionY()
    {
        return m_position_y;
    }
    
    /**
     * Retourne les sprites du projectile
     * @author Romain MONIER, Quentin KAIFFER
     * @return Un tableau de sprites (surfaces)
     */
    public SDLSurface[] getSprites()
    {
        return m_sprites;
    }
    
    /**
     * Retourne le sprite actuel du projectile
     * @author Romain MONIER
     * @return Le sprite dirigé (surface)
     */
    public SDLSurface getSprite()
    {
        return m_sprites[m_direction.ordinal()];
    }
    
    /**
     * Retourne la direction du projectile
     * @author Romain MONIER, Quentin KAIFFER
     * @return La direction
     */
    public DirProjectile getDirection()
    {
        return m_direction;
    }
    
    /**
     * Retourne le son à émettre lors du tir
     * @author Romain MONIER
     * @return Le son (MixChunk)
     */
    public MixChunk getSonTir()
    {
        return m_son_tir;
    }
    
    /**
     * Retourne le son de l'explosion du projectile
     * @author Romain MONIER
     * @return Le son (MixChunk)
     */
    public MixChunk getSonExplosion()
    {
        return m_son_explosion;
    }
    
    /**
     * Indique si sensible à la gravité
     * @author Romain MONIER
     * @return Vrai si sensible
     */
    public boolean isGravite()
    {
        return m_gravite;
    }
    
    /*  SETTER  */
    
    /**
     * Modifie la coordonnée X de la position
     * @author Romain MONIER, Quentin KAIFFER
     * @param position_x La nouvelle position
     */
    public void setPositionX(int position_x)
    {
        m_position_x = position_x;
    }
    
    /**
     * Modifie la coordonnée Y de la position
     * @author Romain MONIER, Quentin KAIFFER
     * @param position_y La nouvelle position
     */
    public void setPositionY(int position_y)
    {
        m_position_y = position_y;
    }
    
    /**
     * Indique si le Projectile est à l'écran
     * @author Romain MONIER, Quentin KAIFFER
     * @param onscreen Vrai si à l'écran
     */
    public void setOnScreen(boolean onscreen)
    {
        m_onscreen = onscreen;
    }
    
    /**
     * Modifie la vitesse du projectile
     * @author Romain MONIER, Quentin KAIFFER
     * @param vitesse La nouvelle vitesse
     */
    public void setVitesse(int vitesse)
    {
        m_vitesse = vitesse;
    }
}
