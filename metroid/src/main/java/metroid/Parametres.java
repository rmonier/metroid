/* FICHIER PARAMETRES.JAVA :
 *      - PARAMETRAGE SONS
 *
 *  DERNIÈRE MÀJ : 29/05/2017 par RM
 *  CRÉÉ PAR MARIN NAVARO
 *  2016/2017
 * ------------------------------------------
 *  INFOS :
 *      - 
 * ------------------------------------------
 */

package metroid;
 
import sdljava.SDLException;
import sdljava.audio.*;
import sdljava.mixer.*;

/** Réglages dans options
 * @author Marin NAVARRO
 */
public class Parametres
{
	/*	ATTRIBUTS STATIQUES (d'où le "s_")	*/
	
	private static boolean s_son = true, s_musique = true;
	
	/*	GETTERS	*/
	
	/**
    * Vérifie si la musique est activée 
    * @author Marin NAVARRO
    * @return Vrai si elle est activée
    */
	public static boolean getMusique() 
	{
		return s_musique;
	}
	
	/**
    * Vérifie si les sons sur les boutons sont activés 
    * @author Marin NAVARRO
    * @return Vrai s'ils sont activés
    */
	public static boolean getSon()
	{
		return s_son;
	}
	
	/*	SETTERS	*/
	
	/**
     * Coupe la musique
     * @author Marin NAVARRO
     * @param musique jouer ou non
     */
	public static void setMusique(boolean musique) throws SDLException, InterruptedException
	{
		s_musique = musique;
		
		if (!s_musique && SDLMixer.playingMusic()){ 		// On met ! pour tester le false / On met juste le nom de la variable pour tester le true
			SDLMixer.haltMusic();
		}
	}
	
	/**
     * Remet la musique ou la désactive
     * @author Marin NAVARRO
     * @param musique jouer ou non
     * @param mus la musique qu'on veut jouer
     */
	public static void setMusique(boolean musique, MixMusic mus) throws SDLException, InterruptedException
	{
		s_musique = musique;
		
		if (!s_musique && SDLMixer.playingMusic()){ 		
			SDLMixer.haltMusic();
		}
		else if (s_musique && !SDLMixer.playingMusic()){ 		
			SDLMixer.playMusic(mus,-1);
		}
	}
	
	/**
     * Coupe le son ou l'active
     * @author Marin NAVARRO
     * @param son ON/OFF
     */
	public static void setSon(boolean son)
	{
		s_son = son;
	}
}
