/* FICHIER MENUS.JAVA :
 *      - MENU PRINCIPAL
 *      - MENU JOUER
 *      - MENU OPTIONS
 *      - MENU CRÉDITS
 *      - QUITTER
 * 
 *  DERNIÈRE MÀJ : 31/05/2017 par MN
 *  CRÉÉ PAR ROMAIN MONIER
 *  2016/2017
 * ------------------------------------------
 *  INFOS :
 *      - 
 * ------------------------------------------
 */

package metroid;
 
import sdljava.SDLException;

import sdljava.SDLMain;

import sdljava.video.SDLVideo;
import sdljava.video.SDLSurface;
import sdljava.video.SDLRect;
import sdljava.video.SDLColor;
import sdljava.image.SDLImage;

import sdljava.event.SDLEvent;
import sdljava.event.SDLMouseButtonEvent;
import sdljava.SDLTimer;

import sdljava.audio.*;
import sdljava.mixer.*;

import sdljava.ttf.SDLTTF;
import sdljava.ttf.SDLTrueTypeFont;

/** Menus
 * @author Romain MONIER
 */
public class Menus
{
	/**
	 * Menu principal
     * @author Romain MONIER, Marin NAVARRO
	 * @param ecran L'écran pour l'affichage
	 * @param son_slide Son pour le slide
	 * @param musique La musique jouée actuellement
	 * @param son_click Son pour le click
	*/
    public static void principal(SDLSurface ecran, MixChunk son_slide, MixChunk son_click, MixMusic musique) throws SDLException, InterruptedException
    {
        SDLEvent event = null;
        boolean continuer = true, jouer_son_slide = false, brillance_fadein = true, dark_fadein = true;
        boolean dessus_bouton[] = {false, false, false, false};
        int transparence_brillance = 0, transparence_dark = 0;
        final int VITESSE_BRILLANCE = 10, VITESSE_DARK = 0;
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        SDLSurface img_principal = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/principal.jpg");
        SDLSurface img_principal_bright = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/principal_bright.jpg");
        SDLSurface img_principal_dark = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/principal_dark.jpg");
        SDLRect pos_img_principal = new SDLRect(0,0);
        
        SDLSurface img_titre = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/titre.png");
        SDLRect pos_img_titre = new SDLRect((ecran.getWidth() / 2) - (img_titre.getWidth() / 2),0);
        
        SDLSurface img_bouton_quitter = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_quitter.png");
        SDLSurface img_bouton_quitter_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_quitter_ok.png");
        SDLRect pos_img_bouton_quitter = new SDLRect((ecran.getWidth() / 4) - (img_bouton_quitter.getWidth() / 2), (ecran.getHeight() - img_bouton_quitter.getHeight()));
       
        SDLSurface img_bouton_credits = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_credits.png");
        SDLSurface img_bouton_credits_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_credits_ok.png");
        SDLRect pos_img_bouton_credits = new SDLRect((ecran.getWidth() / 4) - (img_bouton_credits.getWidth() / 2), (ecran.getHeight() - img_bouton_quitter.getHeight() - img_bouton_credits.getHeight()));
        
        SDLSurface img_bouton_options = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_options.png");
        SDLSurface img_bouton_options_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_options_ok.png");
        SDLRect pos_img_bouton_options = new SDLRect((ecran.getWidth() / 4) - (img_bouton_options.getWidth() / 2), (ecran.getHeight() - img_bouton_quitter.getHeight() - img_bouton_credits.getHeight() - img_bouton_options.getHeight()));
        
        SDLSurface img_bouton_jouer = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_jouer.png");
        SDLSurface img_bouton_jouer_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_jouer_ok.png");
        SDLRect pos_img_bouton_jouer = new SDLRect((ecran.getWidth() / 4) - (img_bouton_jouer.getWidth() / 2), (ecran.getHeight() - img_bouton_quitter.getHeight() - img_bouton_credits.getHeight() - img_bouton_options.getHeight() - img_bouton_jouer.getHeight()));
           
        while(continuer)
        {            
            jouer_son_slide = false; // On ne le passe à true que quand on entre pour la première fois dans la zone
            
            //  BRILLANCE & DARK -------------------------------
            if(transparence_brillance + VITESSE_BRILLANCE >= 255)
                brillance_fadein = false;
            else if(transparence_brillance - VITESSE_BRILLANCE <= 0)
                brillance_fadein = true;
            if(transparence_dark + VITESSE_DARK >= 255)
                dark_fadein = false;
            else if(transparence_dark - VITESSE_DARK <= 0)
                dark_fadein = true;
                
            if(brillance_fadein)
                transparence_brillance += VITESSE_BRILLANCE;
            else
                transparence_brillance -= VITESSE_BRILLANCE;
            if(dark_fadein)
                transparence_dark += VITESSE_DARK;
            else
                transparence_dark -= VITESSE_DARK;
            
            //-------------------------------------------------
            
            /*  EVENEMENTS  */
            
            event = SDLEvent.pollEvent();
            if(event instanceof SDLEvent)
            {
                switch(event.getType())
                {
                    case SDLEvent.SDL_QUIT:
                        continuer = false;
                        break;
                    default:
                        break;
                }
            }
            if(event instanceof SDLMouseButtonEvent)
            {
                if(((SDLMouseButtonEvent)event).getButton() == SDLEvent.SDL_BUTTON_LEFT && event.getType() == SDLEvent.SDL_MOUSEBUTTONDOWN){
                    if(isOnSurface(img_bouton_quitter, pos_img_bouton_quitter)){
						if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
                        continuer = false;
                    }
                    if(isOnSurface(img_bouton_credits, pos_img_bouton_credits)){
                        if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
                        Menus.credits(ecran, son_slide, son_click);
                    }
                    if(isOnSurface(img_bouton_options, pos_img_bouton_options)){
                        if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
                        Menus.options(ecran, son_slide, son_click, musique);
                    }
                    if(isOnSurface(img_bouton_jouer, pos_img_bouton_jouer)){
                        if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
                        if(Parametres.getMusique())
							SDLMixer.fadeOutMusic(1000);
                        Jeu.jouer(ecran, son_click);
                        if(Parametres.getMusique())
							SDLMixer.playMusic(musique, -1);
                    }
                }
            }
            
            /*  AFFICHAGE   */
            
            ecran.fillRect(ecran.mapRGB(0,0,0)); // Nettoyer l'écran
            img_principal.blitSurface(ecran, pos_img_principal);
            
            img_principal_bright.setAlpha(SDLVideo.SDL_SRCALPHA, transparence_brillance);
            img_principal_bright.blitSurface(ecran, pos_img_principal);
            img_principal_dark.setAlpha(SDLVideo.SDL_SRCALPHA, transparence_dark);
            img_principal_dark.blitSurface(ecran, pos_img_principal);
            
            img_titre.blitSurface(ecran, pos_img_titre);
            
            if(isOnSurface(img_bouton_jouer, pos_img_bouton_jouer)){
                if(!dessus_bouton[0]){
                    jouer_son_slide = true;
                    dessus_bouton[0] = true;
                }
                img_bouton_jouer_ok.blitSurface(ecran, pos_img_bouton_jouer);
            }
            else{
                dessus_bouton[0] = false;
                img_bouton_jouer.blitSurface(ecran, pos_img_bouton_jouer);
            }
            if(isOnSurface(img_bouton_options, pos_img_bouton_options)){
                if(!dessus_bouton[1]){
                    jouer_son_slide = true;
                    dessus_bouton[1] = true;
                }
                img_bouton_options_ok.blitSurface(ecran, pos_img_bouton_options);
            }
            else{
                dessus_bouton[1] = false;
                img_bouton_options.blitSurface(ecran, pos_img_bouton_options);
            }
            if(isOnSurface(img_bouton_credits, pos_img_bouton_credits)){
                if(!dessus_bouton[2]){
                    jouer_son_slide = true;
                    dessus_bouton[2] = true;
                }
                img_bouton_credits_ok.blitSurface(ecran, pos_img_bouton_credits);
            }
            else{
                dessus_bouton[2] = false;
                img_bouton_credits.blitSurface(ecran, pos_img_bouton_credits);
            }
            
            if(isOnSurface(img_bouton_quitter, pos_img_bouton_quitter)){
                if(!dessus_bouton[3]){
                    jouer_son_slide = true;
                    dessus_bouton[3] = true;
                }
                img_bouton_quitter_ok.blitSurface(ecran, pos_img_bouton_quitter);
            }
            else{
                dessus_bouton[3] = false;
                img_bouton_quitter.blitSurface(ecran, pos_img_bouton_quitter);
            }
            
            ecran.flip();
            
            /*  JOUER SONS SLIDE */
            
            if(jouer_son_slide && Parametres.getSon()){
                SDLMixer.playChannel(-1, son_slide, 0);
            }
            
            SDLTimer.delay(8);
        }
        
        /*  ON VIDE LA MEMOIRE   */
        
        img_bouton_jouer_ok.freeSurface();
        img_bouton_jouer.freeSurface();
        img_bouton_credits_ok.freeSurface();
        img_bouton_credits.freeSurface();
        img_bouton_options_ok.freeSurface();
        img_bouton_options.freeSurface();
        img_bouton_quitter_ok.freeSurface();
        img_bouton_quitter.freeSurface();
        img_titre.freeSurface();
        img_principal_dark.freeSurface();
        img_principal_bright.freeSurface();
        img_principal.freeSurface();
    }
    
	/**
	 * Menu Options
     * @author Marin NAVARRO
	 * @param ecran L'écran pour l'affichage
	 * @param son_slide Son pour le slide
	 * @param son_click Son pour le click
	 * @param musique La musique jouée actuellement
	*/
    public static void options(SDLSurface ecran, MixChunk son_slide, MixChunk son_click, MixMusic musique) throws SDLException, InterruptedException
    {
		SDLEvent event = null;
        boolean continuer = true, jouer_son_slide = false;
        boolean dessus_bouton[] = {false, false, false};
        SDLSurface img_bouton_musique_actuel, img_bouton_son_actuel, img_bouton_musique_actuel_ok, img_bouton_son_actuel_ok;
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        SDLSurface img_options = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/options.jpg");
        SDLRect pos_img_options = new SDLRect(0,0);
        
        SDLSurface img_titre_options = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/titre_options.png");
        SDLRect pos_img_titre_options = new SDLRect((ecran.getWidth() *3/4) - (img_titre_options.getWidth() / 2),img_titre_options.getHeight()-1280);
        
        SDLSurface img_bouton_retour = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_retour.png");
        SDLSurface img_bouton_retour_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_retour_ok.png");
        SDLRect pos_img_bouton_retour = new SDLRect((ecran.getWidth() *3/4) - (img_bouton_retour.getWidth() / 2), (ecran.getHeight() - img_bouton_retour.getHeight()));
        
        SDLSurface img_bouton_musique_on = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_musique_on.png");
        SDLSurface img_bouton_musique_on_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_musique_on_ok.png");
        SDLRect pos_img_bouton_musique = new SDLRect((ecran.getWidth() *3/4) - (img_bouton_musique_on.getWidth() / 2 - 100), (ecran.getHeight() - 5*img_bouton_retour.getHeight()  ));
        
        SDLSurface img_bouton_musique_off = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_musique_off.png");
        SDLSurface img_bouton_musique_off_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_musique_off_ok.png");
        
        SDLSurface img_bouton_son_on = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_son_on.png");
        SDLSurface img_bouton_son_on_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_son_on_ok.png");
        SDLRect pos_img_bouton_son = new SDLRect((ecran.getWidth() *3/4) - (img_bouton_son_on.getWidth() / 2 + 100), (ecran.getHeight() - 5*img_bouton_retour.getHeight()));
        
        SDLSurface img_bouton_son_off = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_son_off.png");
        SDLSurface img_bouton_son_off_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_son_off_ok.png");
            
       if(Parametres.getMusique()){
			img_bouton_musique_actuel = img_bouton_musique_on;
			img_bouton_musique_actuel_ok = img_bouton_musique_on_ok;
		}
		else{
			img_bouton_musique_actuel = img_bouton_musique_off;
			img_bouton_musique_actuel_ok = img_bouton_musique_off_ok;
		}
            
        if(Parametres.getSon()){
			img_bouton_son_actuel = img_bouton_son_on;
			img_bouton_son_actuel_ok = img_bouton_son_on_ok;
		}
		else{
			img_bouton_son_actuel = img_bouton_son_off;
			img_bouton_son_actuel_ok = img_bouton_son_off_ok;
		}
        
        while(continuer)
        {            
            jouer_son_slide = false;
            
            event = SDLEvent.pollEvent();
            if(event instanceof SDLEvent)
            {
                switch(event.getType())
                {
                    case SDLEvent.SDL_QUIT:
                        ecran.freeSurface();                        
                        SDLTTF.quit();        
                        SDLMain.quit(); 
                        System.exit(0);        
                        break;
                    default:
                        break;
                }
            }
            if(event instanceof SDLMouseButtonEvent)
            {
                if(((SDLMouseButtonEvent)event).getButton() == SDLEvent.SDL_BUTTON_LEFT && event.getType() == SDLEvent.SDL_MOUSEBUTTONDOWN){
                    if(isOnSurface(img_bouton_retour, pos_img_bouton_retour)){
                        if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
                        continuer = false;
                    }
                }
                if(((SDLMouseButtonEvent)event).getButton() == SDLEvent.SDL_BUTTON_LEFT && event.getType() == SDLEvent.SDL_MOUSEBUTTONDOWN){
                    if(isOnSurface(img_bouton_musique_actuel, pos_img_bouton_musique)){
                        if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
							
						if(Parametres.getMusique())
							Parametres.setMusique(!Parametres.getMusique());
						else
							Parametres.setMusique(!Parametres.getMusique(), musique);
                    }
                }
                if(((SDLMouseButtonEvent)event).getButton() == SDLEvent.SDL_BUTTON_LEFT && event.getType() == SDLEvent.SDL_MOUSEBUTTONDOWN){
                    if(isOnSurface(img_bouton_son_actuel, pos_img_bouton_son)){
                        if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
						
						Parametres.setSon(!Parametres.getSon());
                    }
                }
            }
            
            /*  AFFICHAGE   */
            
            ecran.fillRect(ecran.mapRGB(0,0,0)); // Nettoyer l'écran
            img_options.blitSurface(ecran, pos_img_options);
            img_titre_options.blitSurface(ecran, pos_img_titre_options);
            
            if(Parametres.getMusique()){
				img_bouton_musique_actuel = img_bouton_musique_on;
				img_bouton_musique_actuel_ok = img_bouton_musique_on_ok;
			}
			else{
				img_bouton_musique_actuel = img_bouton_musique_off;
				img_bouton_musique_actuel_ok = img_bouton_musique_off_ok;
			}
            
            if(Parametres.getSon()){
				img_bouton_son_actuel = img_bouton_son_on;
				img_bouton_son_actuel_ok = img_bouton_son_on_ok;
			}
			else{
				img_bouton_son_actuel = img_bouton_son_off;
				img_bouton_son_actuel_ok = img_bouton_son_off_ok;
			}
            
            if(isOnSurface(img_bouton_son_actuel, pos_img_bouton_son)){
                if(!dessus_bouton[0]){
                    jouer_son_slide = true;
                    dessus_bouton[0] = true;
                }
                img_bouton_son_actuel_ok.blitSurface(ecran, pos_img_bouton_son);
            }
            else{
                dessus_bouton[0] = false;
                img_bouton_son_actuel.blitSurface(ecran, pos_img_bouton_son);
            }
              
            if(isOnSurface(img_bouton_musique_actuel, pos_img_bouton_musique)){
                if(!dessus_bouton[1]){
                    jouer_son_slide = true;
                    dessus_bouton[1] = true;
                }
                img_bouton_musique_actuel_ok.blitSurface(ecran, pos_img_bouton_musique);
            }
            else{
                dessus_bouton[1] = false;
                img_bouton_musique_actuel.blitSurface(ecran, pos_img_bouton_musique);
            }
       
            if(isOnSurface(img_bouton_retour, pos_img_bouton_retour)){
                if(!dessus_bouton[2]){
                    jouer_son_slide = true;
                    dessus_bouton[2] = true;
                }
                img_bouton_retour_ok.blitSurface(ecran, pos_img_bouton_retour);
            }
            else{
                dessus_bouton[2] = false;
                img_bouton_retour.blitSurface(ecran, pos_img_bouton_retour);
            }
           
            ecran.flip();
            
            /*  JOUER SONS SLIDE */
            
            if(jouer_son_slide && Parametres.getSon()){
                SDLMixer.playChannel(-1, son_slide, 0);
            }
            
            SDLTimer.delay(10);
        }
        
		/*  ON VIDE LA MEMOIRE  */
        
        img_bouton_musique_on_ok.freeSurface();
        img_bouton_musique_on.freeSurface();
        img_bouton_musique_off_ok.freeSurface();
        img_bouton_musique_off.freeSurface();
        img_bouton_son_on_ok.freeSurface();
        img_bouton_son_on.freeSurface();
        img_bouton_son_off_ok.freeSurface();
        img_bouton_son_off.freeSurface();
        img_bouton_retour_ok.freeSurface();
        img_bouton_retour.freeSurface();  
        img_titre_options.freeSurface(); 
        img_options.freeSurface(); 
        
    }
    
	/**
	 * Menu Crédits
     * @author Marin NAVARRO
	 * @param ecran L'écran pour l'affichage
	 * @param son_slide Son pour le slide
	 * @param son_click Son pour le click
	*/
    public static void credits(SDLSurface ecran, MixChunk son_slide, MixChunk son_click) throws SDLException, InterruptedException
    {
		SDLEvent event = null;
        boolean continuer = true, jouer_son_slide = false;
        boolean dessus_bouton[] = {false};
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        SDLSurface img_credits = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/credits.png");
        SDLRect pos_img_credits = new SDLRect(0,0);
        
        SDLSurface img_bouton_retour = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_retour.png");
        SDLSurface img_bouton_retour_ok = SDLImage.load(System.getProperty("metroid.resources") + "img/buttons/bouton_retour_ok.png");
        SDLRect pos_img_bouton_retour = new SDLRect((ecran.getWidth() / 2) - (img_bouton_retour.getWidth() / 2), (ecran.getHeight() - img_bouton_retour.getHeight()));
     
        while(continuer)
        {            
            jouer_son_slide = false;
            
            event = SDLEvent.pollEvent();
            if(event instanceof SDLEvent)
            {
                switch(event.getType())
                {
                    case SDLEvent.SDL_QUIT:
                        ecran.freeSurface();                        
                        SDLTTF.quit();        
                        SDLMain.quit(); 
                        System.exit(0);      
                        break;
                    default:
                        break;
                }
            }
            if(event instanceof SDLMouseButtonEvent)
            {
                if(((SDLMouseButtonEvent)event).getButton() == SDLEvent.SDL_BUTTON_LEFT && event.getType() == SDLEvent.SDL_MOUSEBUTTONDOWN){
                    if(isOnSurface(img_bouton_retour, pos_img_bouton_retour)){
                        if(Parametres.getSon())
							SDLMixer.playChannel(-1, son_click, 0);
                        continuer = false;
                    }
                }
            }
            
            /*  AFFICHAGE   */
            
            ecran.fillRect(ecran.mapRGB(0,0,0)); // Nettoyer l'écran
            img_credits.blitSurface(ecran, pos_img_credits);
            
            if(isOnSurface(img_bouton_retour, pos_img_bouton_retour)){
                if(!dessus_bouton[0]){
                    jouer_son_slide = true;
                    dessus_bouton[0] = true;
                }
                img_bouton_retour_ok.blitSurface(ecran, pos_img_bouton_retour);
            }
            else{
                dessus_bouton[0] = false;
                img_bouton_retour.blitSurface(ecran, pos_img_bouton_retour);
            }
           
            ecran.flip();
            
            /*  JOUER SONS SLIDE */
            
            if(jouer_son_slide && Parametres.getSon()){
                SDLMixer.playChannel(-1, son_slide, 0);
            }
            
            SDLTimer.delay(10);
        }
        
		/*  ON VIDE LA MEMOIRE  */
        
        img_bouton_retour_ok.freeSurface();
        img_bouton_retour.freeSurface();  
        img_credits.freeSurface(); 
    }
    
	/**
	 * Vérifier zone bouton
     * @author Romain MONIER
	 * @param image Le bouton
	 * @param pos_image Sa position
     * @return Vrai si souris sur bouton
	*/
    public static boolean isOnSurface(SDLSurface image, SDLRect pos_image) throws SDLException, InterruptedException
    {   
        if(SDLEvent.getMouseState().getX() >= pos_image.getX() && SDLEvent.getMouseState().getX() <= pos_image.getX() + image.getWidth() && SDLEvent.getMouseState().getY() >= pos_image.getY() && SDLEvent.getMouseState().getY() <= pos_image.getY() + image.getHeight())
            return true;
        
        return false;
    }
}
