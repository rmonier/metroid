/* FICHIER JEU.JAVA :
 *      - INTERFACE DE JEU (combats)
 * 
 *  DERNIÈRE MÀJ : 31/05/2017 par RM
 *  CRÉÉ PAR ROMAIN MONIER
 *  2016/2017
 * ------------------------------------------
 *  INFOS :
 *      - ? chercher un moyen pour que le projectile soit toujours en face de l'arme lorsqu'il est tiré
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
import sdljava.event.SDLKeyboardEvent;
import sdljava.event.SDLKey;
import sdljava.SDLTimer;

import sdljava.audio.*;
import sdljava.mixer.*;

import sdljava.ttf.SDLTTF;
import sdljava.ttf.SDLTrueTypeFont;

import java.util.concurrent.ThreadLocalRandom;

/** Jeu
 * @author Romain MONIER
 */
public class Jeu
{
	/**
	 * Lancer le combat
     * @author Romain MONIER
	 * @param ecran L'écran pour l'affichage
	 * @param son_click Le son du click
	*/
    public static void jouer(SDLSurface ecran, MixChunk son_click) throws SDLException, InterruptedException
    {
        SDLEvent event = null;
        boolean continuer = true, keyIsDown = false, fin_combat = false, saut_joueur = false, brillance_fadein = true;
        final int PROJECTILES_ONSCREEN_MAX = 1000, ARME_SAMUS_Y = 28, NOMBRE_BOSS = 3, VITESSE_BRILLANCE = 10;
        DirProjectile direction_projectile;
        Projectile onscreen_projectiles[] = new Projectile[PROJECTILES_ONSCREEN_MAX];
        Personnage ennemi = null;
        int fade_out_music = 1000, boss = 1, transparence_brillance = 0;
        SDLSurface img_fond = null, img_fond_brillante = null, img_sol = null;
        MixMusic musique = null;
        
        /*  CHOIX ALEATOIRE DU BOSS AVEC SON TERRAIN    */
        
        boss = ThreadLocalRandom.current().nextInt(1, NOMBRE_BOSS + 1);
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        if(boss == 1){
            img_fond = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/caverne.jpg");
            img_fond_brillante = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/caverne_bright.jpg");
            img_sol = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/sol_caverne.png");
        }
        else if(boss == 2){
            img_fond = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/lave.jpg");
            img_fond_brillante = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/lave_bright.jpg");
            img_sol = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/sol_lave.png");
        }
        else if(boss == 3){
            img_fond = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/apocalypse.jpg");
            img_fond_brillante = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/apocalypse_bright.jpg");
            img_sol = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/sol_apocalypse.png");
        }
        
        SDLRect pos_img_fond = new SDLRect(0,0);
        SDLRect pos_img_sol = new SDLRect(0, (ecran.getHeight() - img_sol.getHeight()));
        
        SDLSurface img_victoire = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/victoire.gif");
        SDLRect pos_img_victoire = new SDLRect((ecran.getWidth() - img_victoire.getWidth()) / 2, (ecran.getHeight() - img_victoire.getHeight()) / 4);
        
        SDLSurface img_defaite = SDLImage.load(System.getProperty("metroid.resources") + "img/backgrounds/defaite.gif");
        SDLRect pos_img_defaite = new SDLRect((ecran.getWidth() - img_defaite.getWidth()) / 2, (ecran.getHeight() - img_defaite.getHeight()) / 4);
        
        /*  CHARGEMENT AUDIO    */
        
        if(boss == 1)
            musique = SDLMixer.loadMUS(System.getProperty("metroid.resources") + "audio/msc/sector1.wav");
        else if(boss == 2)
            musique = SDLMixer.loadMUS(System.getProperty("metroid.resources") + "audio/msc/ridley2.wav");
        else if(boss == 3)
            musique = SDLMixer.loadMUS(System.getProperty("metroid.resources") + "audio/msc/ridley.wav");
            
        MixMusic defaite = SDLMixer.loadMUS(System.getProperty("metroid.resources") + "audio/msc/defaite.wav");
        MixMusic victoire = SDLMixer.loadMUS(System.getProperty("metroid.resources") + "audio/msc/victoire.wav");
        MixChunk son_munvide = SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/munvide.ogg");
        
        if(Parametres.getMusique())
            SDLMixer.playMusic(musique, -1);
        
        /*  CREATION DES PERSONNAGES   */
        
        Personnage samus = new Personnage("SAMUS", 500, 8, 40, 300,
                                          new SDLSurface[]
                                          {
                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/samus_droite_1.png"),
                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/samus_gauche_1.png"),
                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/samus_haut_1.png"),
                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/samus_bas_1.png")
                                          }, 
                                          new Arme("BOLTER", 10, -1, 30, false, SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/bolter.ogg"), SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/explosion_bolter.ogg"),
                                                    new SDLSurface[]
                                                    {
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball_shot_1.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball_explosion_1.png")
                                                    }
                                                  ),
                                          new Arme("LANCE-ROCKET", 50, 10, 60, false, SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/rocket.ogg"), SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/explosion_rocket.ogg"),
                                                    new SDLSurface[]
                                                    {
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_droite_1.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_gauche_1.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_haut_1.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_bas_1.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_shot_1.png"),
                                                        SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_explosion_1.png")
                                                    }
                                                  )
                                          );
               
        if(boss == 1){
            ennemi = new Personnage("SPACE PIRATE", 1500, 10, 50, 350,
                                              new SDLSurface[]
                                              {
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/spacepirate_droite_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/spacepirate_gauche_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/spacepirate_haut_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/spacepirate_bas_1.png")
                                              }, 
                                              new Arme("POISONBALL", 10, -1, 20, false, SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/poisonball.ogg"), SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/explosion_poisonball.ogg"),
                                                        new SDLSurface[]
                                                        {
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball_shot_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball_explosion_1.png")
                                                        }
                                                      )
                                              );
        }
        else if(boss == 2){
            ennemi = new Personnage("RIDLEY", 3000, 30, 100, 400,
                                              new SDLSurface[]
                                              {
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridley_droite_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridley_gauche_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridley_haut_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridley_bas_1.png")
                                              }, 
                                              new Arme("RIDLEY-FIRE", 40, -1, 60, false, SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/ridleyfire.ogg"), SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/explosion_ridleyfire.ogg"),
                                                        new SDLSurface[]
                                                        {
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfire_droite_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfire_gauche_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfire_haut_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfire_bas_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball_shot_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball_explosion_1.png")
                                                        }
                                                      ),
                                              new Arme("RIDLEY-FURY", 100, 1, 60, false, SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/ridleyfury.ogg"), SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/explosion_ridleyfury.ogg"),
                                                        new SDLSurface[]
                                                        {
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfury_droite_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfury_gauche_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfury_haut_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfury_bas_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/poison_ball_shot_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/ridleyfury_explosion_1.png")
                                                        }
                                                      )
                                              );
        }
        else if(boss == 3){
            ennemi = new Personnage("DARK SAMUS", 1000, 8, 40, 300,
                                              new SDLSurface[]
                                              {
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/darksamus_droite_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/darksamus_gauche_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/darksamus_haut_1.png"),
                                                SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/darksamus_bas_1.png")
                                              }, 
                                              new Arme("BOLTER", 10, -1, 30, false, SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/bolter.ogg"), SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/explosion_bolter.ogg"),
                                                        new SDLSurface[]
                                                        {
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball_shot_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/bolter_ball_explosion_1.png")
                                                        }
                                                      ),
                                              new Arme("LANCE-ROCKET", 50, 10, 60, false, SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/rocket.ogg"), SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/explosion_rocket.ogg"),
                                                        new SDLSurface[]
                                                        {
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_droite_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_gauche_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_haut_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_bas_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_shot_1.png"),
                                                            SDLImage.load(System.getProperty("metroid.resources") + "img/sprites/rocket_explosion_1.png")
                                                        }
                                                      )
                                              );
        }
        
        /*  PLACEMENT PERSONNAGES (au niveau du sol)   */

        samus.setPositionX(10);
        samus.setPositionY(pos_img_sol.getY() - samus.getSprite().getHeight());
        samus.setDirection(DirSprite.DROITE);
        
        ennemi.setPositionX(img_sol.getWidth() - ennemi.getSprite().getWidth() - 50);
        ennemi.setPositionY(pos_img_sol.getY() - ennemi.getSprite().getHeight());
        ennemi.setDirection(DirSprite.GAUCHE);
        
        /*  BOUCLE DE COMBAT    */
        
        while(continuer)
        {            
            samus.setSaut(false);
            
            /*  BRILLANCE    */
            
            if(transparence_brillance + VITESSE_BRILLANCE >= 255)
                brillance_fadein = false;
            else if(transparence_brillance - VITESSE_BRILLANCE <= 0)
                brillance_fadein = true;
            if(brillance_fadein)
                transparence_brillance += VITESSE_BRILLANCE;
            else
                transparence_brillance -= VITESSE_BRILLANCE;
            
            /*  RECUPERATION POSITION PERSO POUR MISSILE    */
            
            switch(samus.getDirection())
            {
                case DROITE:
                    direction_projectile = DirProjectile.DROITE;
                    break;
                case GAUCHE:
                    direction_projectile = DirProjectile.GAUCHE;
                    break;
                default:
                    direction_projectile = DirProjectile.DROITE;
                    break;
            }
            
            /*  ON FAIT AVANCER LES PROJECTILES */
            
            for(int i = 0 ; i < onscreen_projectiles.length ; i++)
            {
                if(onscreen_projectiles[i] instanceof Projectile && onscreen_projectiles[i].isOnScreen())
                {
                    switch(onscreen_projectiles[i].getDirection())
                    {
                        case DROITE:
                            onscreen_projectiles[i].setPositionX(onscreen_projectiles[i].getPositionX() + onscreen_projectiles[i].getVitesse());
                            break;
                        case GAUCHE:
                            onscreen_projectiles[i].setPositionX(onscreen_projectiles[i].getPositionX() - onscreen_projectiles[i].getVitesse());
                            break;
                        default:
                            break;
                    }
                }
            }    
            
            /*  EVENEMENTS  */
            
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
                    case SDLEvent.SDL_KEYDOWN:
                        keyIsDown = true;
                        break;
                    case SDLEvent.SDL_KEYUP:
                        keyIsDown = false;
                        break;
                    default:
                        break;
                }
            }
            
            /*  ACTION ENNEMI   */
            
            actionIA(ennemi, samus, ecran, img_sol, pos_img_sol, onscreen_projectiles, son_munvide, ARME_SAMUS_Y);
            
            /*  EVENEMENTS JOUEUR   */
            
            if(event instanceof SDLKeyboardEvent)
            {
                //  On traite les touches classiques
                switch(((SDLKeyboardEvent)event).getSym())
                {
                    case SDLKey.SDLK_ESCAPE:
                        if(Parametres.getSon())
                            SDLMixer.playChannel(-1, son_click, 0);
                        continuer = false;
                        break;
                    
                    case SDLKey.SDLK_LSHIFT:
                        if(keyIsDown){
                            // courrir true
                            samus.setVitesse((int)samus.getVitesse() * 2);
                        }
                        else{
                            // courrir false
                            samus.setVitesse((int)samus.getVitesse() / 2);
                        }
                        break;
                        
                    case SDLKey.SDLK_UP:
                        if(keyIsDown)
                            samus.setDirection(DirSprite.DERRIERE);
                        break;
                        
                    case SDLKey.SDLK_DOWN:
                        if(keyIsDown)
                            samus.setDirection(DirSprite.DEVANT);
                        break;
                    
                    case SDLKey.SDLK_LEFT:
                        if(keyIsDown)
                        {
                            samus.setDirection(DirSprite.GAUCHE);
                            
                            int nouvelle_pos = (samus.getPositionX() - samus.getVitesse());
                            if(isOnTerrain(samus.getSprite(), new SDLRect(nouvelle_pos, samus.getPositionY()), new SDLRect(ecran.getWidth(), pos_img_sol.getY()), new SDLRect(0,0))) {
                                samus.setPositionX(nouvelle_pos);
                            }
                            
                        }
                        break;
                        
                    case SDLKey.SDLK_RIGHT:
                        if(keyIsDown)
                        {
                            samus.setDirection(DirSprite.DROITE);
                        
                            int nouvelle_pos = (samus.getPositionX() + samus.getVitesse());
                            if(isOnTerrain(samus.getSprite(), new SDLRect(nouvelle_pos, samus.getPositionY()), new SDLRect(ecran.getWidth(), pos_img_sol.getY()), new SDLRect(0,0))) {
                                samus.setPositionX(nouvelle_pos);
                            }
                        
                        }
                        break;
                        
                    //	On traite la fonction saut
                    case SDLKey.SDLK_SPACE:
                        if(keyIsDown) {
                            saut_joueur = true;
                        } else {
                            saut_joueur = false;
                            samus.setSaut(false);
                        }
                        break;
                        
                    default:
                        break;
                }
                    
                //  On traite les touches variables selon les claviers                
                switch(((SDLKeyboardEvent)event).getUnicode())
                {
                    // ARME PRINCIPALE (on ignore si c'est une majuscule ou une minuscule)
                    case SDLKey.SDLK_a:
                    case 'A':
                        if(keyIsDown)
                        {
                            if(samus.getDirection() != DirSprite.DEVANT && samus.getDirection() != DirSprite.DERRIERE)
                            {
                                if(samus.tirerArmePrincipale())
                                {
                                    for(int i = 0 ; i < onscreen_projectiles.length ; i++)
                                    {
                                        if(!(onscreen_projectiles[i] instanceof Projectile) || !(onscreen_projectiles[i].isOnScreen()))
                                        {
                                            onscreen_projectiles[i] = samus.getArmePrincipale().tirer(direction_projectile);
                                            if(Parametres.getSon())
                                                SDLMixer.playChannel(-1, onscreen_projectiles[i].getSonTir(), 0);
                                            
                                            int position_arme_x = 0;
                                            if(samus.getDirection() == DirSprite.DROITE)
                                                position_arme_x = samus.getSprite().getWidth();
                                            else if(samus.getDirection() == DirSprite.GAUCHE)
                                                position_arme_x = -onscreen_projectiles[i].getSprite().getWidth();
                                                
                                            onscreen_projectiles[i].setPositionX(samus.getPositionX()+position_arme_x);
                                            onscreen_projectiles[i].setPositionY(samus.getPositionY()+ARME_SAMUS_Y);
                                            
                                            break;
                                        }
                                    } 
                                }   
                                else 
                                {
                                    if(Parametres.getSon())
                                        SDLMixer.playChannel(-1, son_munvide, 0);
                                }                                
                            }
                        }
                        break;
                        
                    // ARME SECONDAIRE (on ignore si c'est une majuscule ou une minuscule)
                    case SDLKey.SDLK_z:
                    case 'Z':
                        if(keyIsDown)
                        {
                            if(samus.getDirection() != DirSprite.DEVANT && samus.getDirection() != DirSprite.DERRIERE)
                            {
                                if(samus.tirerArmeSecondaire())
                                {
                                    for(int i = 0 ; i < onscreen_projectiles.length ; i++)
                                    {
                                        if(!(onscreen_projectiles[i] instanceof Projectile) || !(onscreen_projectiles[i].isOnScreen()))
                                        {
                                            onscreen_projectiles[i] = samus.getArmeSecondaire().tirer(direction_projectile);
                                            if(Parametres.getSon())
                                                SDLMixer.playChannel(-1, onscreen_projectiles[i].getSonTir(), 0);
                                            
                                            int position_arme_x = 0;
                                            if(samus.getDirection() == DirSprite.DROITE)
                                                position_arme_x = samus.getSprite().getWidth();
                                            else if(samus.getDirection() == DirSprite.GAUCHE)
                                                position_arme_x = -onscreen_projectiles[i].getSprite().getWidth();
                                                
                                            onscreen_projectiles[i].setPositionX(samus.getPositionX()+position_arme_x);
                                            onscreen_projectiles[i].setPositionY(samus.getPositionY()+ARME_SAMUS_Y);
                                            
                                            break;
                                        }
                                    } 
                                }   
                                else 
                                {
                                    if(Parametres.getSon())
                                        SDLMixer.playChannel(-1, son_munvide, 0);
                                }                                   
                            }
                        }
                        break;
                        
                    default:
                        break;
                }
            }   
            
            /*  GESTION SAUT JOUEUR (pour palier le probleme de KeyRepeat)    */
            
            if(saut_joueur)
            {
                if(samus.getNiveauSaut() < samus.getNiveauSautMax())
                {
                    samus.setSaut(true);
                    samus.sauter();
                    samus.setPositionY(samus.getPositionY() - samus.getVitesseSaut());
                }
                else
                { 
                    saut_joueur = false;
                    samus.setSaut(false);
                }
            }
            
            /*	ON APPLIQUE LA GRAVITE	*/
            
            appliquerGravite(samus, ecran, img_sol);
            appliquerGravite(ennemi, ecran, img_sol);
            for(int i = 0 ; i < onscreen_projectiles.length ; i++)
            {
                if(onscreen_projectiles[i] instanceof Projectile && onscreen_projectiles[i].isOnScreen() && !onscreen_projectiles[i].isExploding() && onscreen_projectiles[i].isGravite())
                {
                    appliquerGravite(onscreen_projectiles[i], ecran, img_sol);
                }
            }
            
            /*  ON CHECK LES COLLISIONS */
            
            //  Projectiles
            for(int i = 0 ; i < onscreen_projectiles.length ; i++)
            {
                if(onscreen_projectiles[i] instanceof Projectile && onscreen_projectiles[i].isOnScreen() && !onscreen_projectiles[i].isExploding())
                {
                    //  Samus
                    if(collision(samus.getSprite(), samus.getPosition(), onscreen_projectiles[i].getSprite(), onscreen_projectiles[i].getPosition())){
                        onscreen_projectiles[i].explosion(samus);
                        if(Parametres.getSon())
                            SDLMixer.playChannel(-1, onscreen_projectiles[i].getSonExplosion(), 0);
                    }
                        
                    //  Ennemi
                    else if(collision(ennemi.getSprite(), ennemi.getPosition(), onscreen_projectiles[i].getSprite(), onscreen_projectiles[i].getPosition())){
                        onscreen_projectiles[i].explosion(ennemi);
                        if(Parametres.getSon())
                            SDLMixer.playChannel(-1, onscreen_projectiles[i].getSonExplosion(), 0);
                    }
                }
            }
            
            /*  ON VERIFIE SI LE COMBAT EST FINI    */
            
            if(!samus.isAlive()){
                //samus direction (sprite) à mort
                fin_combat = true;
            }
            else if(!ennemi.isAlive()){
                //ennemi direction (sprite) à mort
                fin_combat = true;
            }
            
            /*  AFFICHAGE   */
            
            ecran.fillRect(ecran.mapRGB(0,0,0)); // Nettoyer l'écran
            img_fond.blitSurface(ecran, pos_img_fond);
            img_fond_brillante.setAlpha(SDLVideo.SDL_SRCALPHA, transparence_brillance);
            img_fond_brillante.blitSurface(ecran, pos_img_fond);
            img_sol.blitSurface(ecran, pos_img_sol);
            
            blitTexte(samus, ecran, new SDLRect(50, pos_img_sol.getY() + 10));
            blitTexte(ennemi, ecran, new SDLRect(ecran.getWidth() - 600, pos_img_sol.getY() + 10));
            
            ennemi.getSprite().blitSurface(ecran, ennemi.getPosition());
            
            samus.getSprite().blitSurface(ecran, samus.getPosition());
            
            for(int i = 0 ; i < onscreen_projectiles.length ; i++)
            {
                if(onscreen_projectiles[i] instanceof Projectile && onscreen_projectiles[i].isOnScreen())
                {
                    SDLSurface sprite_projectile = onscreen_projectiles[i].getSprite();
                    SDLRect pos_projectile = onscreen_projectiles[i].getPosition();
                    
                    if(isOnTerrain(sprite_projectile, pos_projectile, new SDLRect(ecran.getWidth(), pos_img_sol.getY()), new SDLRect(0,0)))
                        sprite_projectile.blitSurface(ecran, pos_projectile);
                    else
                        onscreen_projectiles[i].setOnScreen(false);
                        
                    if(onscreen_projectiles[i].isExploding())
                        onscreen_projectiles[i].setOnScreen(false);
                }
            }
            
            //----- AFFICHAGE DE L'EFFET DE TIR -------------
            
            if(samus.getArmePrincipale().isLance()){
                if(samus.getDirection() == DirSprite.GAUCHE)
                    samus.getArmePrincipale().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(samus.getPositionX() - 20, samus.getPositionY() + ARME_SAMUS_Y));
                else if(samus.getDirection() == DirSprite.DROITE)
                    samus.getArmePrincipale().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(samus.getPositionX() + samus.getSprite().getWidth() - 5, samus.getPositionY() + ARME_SAMUS_Y));
                samus.getArmePrincipale().setLance();
            }
            if(samus.getArmeSecondaire().isLance()){
                if(samus.getDirection() == DirSprite.GAUCHE)
                    samus.getArmeSecondaire().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(samus.getPositionX() - 20, samus.getPositionY() + ARME_SAMUS_Y + 4));
                else if(samus.getDirection() == DirSprite.DROITE)
                    samus.getArmeSecondaire().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(samus.getPositionX() + samus.getSprite().getWidth() - 5, samus.getPositionY() + ARME_SAMUS_Y + 4));
                samus.getArmeSecondaire().setLance();
            }
            if(ennemi.getArmePrincipale().isLance()){
                if(ennemi.getDirection() == DirSprite.GAUCHE)
                    ennemi.getArmePrincipale().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(ennemi.getPositionX() - 20, ennemi.getPositionY() + ARME_SAMUS_Y + 7));
                else if(ennemi.getDirection() == DirSprite.DROITE)
                    ennemi.getArmePrincipale().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(ennemi.getPositionX() + ennemi.getSprite().getWidth() - 5, ennemi.getPositionY()));
                ennemi.getArmePrincipale().setLance();
            }
            if(ennemi.getArmeSecondaire().isLance()){
                if(ennemi.getDirection() == DirSprite.GAUCHE)
                    ennemi.getArmeSecondaire().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(ennemi.getPositionX() - 20, ennemi.getPositionY() + ARME_SAMUS_Y + 7));
                else if(ennemi.getDirection() == DirSprite.DROITE)
                    ennemi.getArmeSecondaire().getSprites()[DirProjectile.SHOT.ordinal()].blitSurface(ecran, new SDLRect(ennemi.getPositionX() + ennemi.getSprite().getWidth() - 5, ennemi.getPositionY()));
                ennemi.getArmeSecondaire().setLance();
            }
            
            //-----------------------------------------------
            
            /*  FIN DU COMBAT SI NECESSAIRE */
            
            if(fin_combat)
            {
                if(Parametres.getMusique())
                    SDLMixer.fadeOutMusic(50);
                if(samus.isAlive()){
                    img_victoire.blitSurface(ecran, pos_img_victoire);
                    if(Parametres.getMusique())
                        SDLMixer.playMusic(victoire, 0);
                }
                else{
                    img_defaite.blitSurface(ecran, pos_img_defaite);
                    if(Parametres.getMusique())
                        SDLMixer.playMusic(defaite, 0);
                }
                
                fade_out_music = 5000;
                
                continuer = false;
            }
            
            ecran.flip();
            
            SDLTimer.delay(8);
        }
        
        if(Parametres.getMusique())
            SDLMixer.fadeOutMusic(fade_out_music);
        else
            SDLTimer.delay(fade_out_music);
        
        /*  ON VIDE LA MEMOIRE   */
        
        samus.destroy();
        ennemi.destroy();
        
        img_victoire.freeSurface();
        img_defaite.freeSurface();
        img_sol.freeSurface();
        img_fond_brillante.freeSurface();
        img_fond.freeSurface();
        
        SDLMixer.freeMusic(victoire);
        SDLMixer.freeMusic(defaite);
        SDLMixer.freeMusic(musique);
    }
    
	/**
	 * Vérifier zone terrain
     * @author Romain MONIER
	 * @param image Le sprite
	 * @param pos_image Sa position
	 * @param terrain_max Position la plus grande autorisée (coin inférieur droit du rectangle du terrain)
	 * @param terrain_min Position la plus petite autorisée (coin supérieur gauche du rectangle du terrain)
     * @return Vrai si sprite dans terrain
	*/
    public static boolean isOnTerrain(SDLSurface image, SDLRect pos_image, SDLRect terrain_max, SDLRect terrain_min)
    {   
        if(pos_image.getX() >= terrain_min.getX() && pos_image.getX() <= terrain_max.getX() - image.getWidth() && pos_image.getY() >= terrain_min.getY() && pos_image.getY() <= terrain_max.getY() + image.getHeight())
            return true;
        
        return false;
    }
    
	/**
	 * Vérifier collisions sprites
     * @author Romain MONIER
	 * @param sprite_1 Premier sprite
	 * @param pos_sprite_1 Sa position
	 * @param sprite_2 Deuxième sprite
	 * @param pos_sprite_2 Sa position
     * @return Vrai si sprites en collision
	*/
    public static boolean collision(SDLSurface sprite_1, SDLRect pos_sprite_1, SDLSurface sprite_2, SDLRect pos_sprite_2)
    {   
        if(pos_sprite_1.getX() < pos_sprite_2.getX() + sprite_2.getWidth() && pos_sprite_1.getX() + sprite_1.getWidth() > pos_sprite_2.getX() && pos_sprite_1.getY() < pos_sprite_2.getY() + sprite_2.getHeight() && pos_sprite_1.getY() + sprite_1.getHeight() > pos_sprite_2.getY())
            return true;
        
        return false;
    }
    
	/**
	 * Récupère des informations sur le Personnage et rafraîchit le texte en conséquence pour ensuite le blitter
     * @author Romain MONIER
	 * @param perso Le personnage auquel on veut récupérer le texte à afficher
	 * @param parent Le personnage auquel on veut récupérer le texte à afficher
	 * @param position Le personnage auquel on veut récupérer le texte à afficher
	*/
    public static void blitTexte(Personnage perso, SDLSurface parent, SDLRect position) throws SDLException, InterruptedException
    {   
        String texte_arme_secondaire = "", texte_munitions = "";
        SDLColor blanc = new SDLColor(255,255,255), rouge = new SDLColor(255,0,0), gris = new SDLColor(150,150,150);     
        SDLColor couleur_pv = blanc, couleur_armes = blanc, couleur_munitions_principales = blanc, couleur_munitions_secondaires = blanc;     
        SDLTrueTypeFont police = SDLTTF.openFont(System.getProperty("metroid.resources") + "font/SquadaOne-Regular.ttf", 30);
        
        if(!perso.getArmeUnique())
        {
            if(perso.getArmeSecondaire().getMunitions() != -1)
                texte_munitions = " MUNITIONS: " + perso.getArmeSecondaire().getMunitions() + "/" + perso.getArmeSecondaire().getMunitionsMax();
            else 
                texte_munitions = " MUNITIONS: ø ";
            
            texte_arme_secondaire = perso.getArmeSecondaire().getNom() + " -> " + " MUNITIONS: " + perso.getArmeSecondaire().getMunitions() + "/" + perso.getArmeSecondaire().getMunitionsMax() + " DÉGATS: " + perso.getArmeSecondaire().getDegats();
        }
        
        if(perso.getArmePrincipale().getMunitions() != -1)
            texte_munitions = " MUNITIONS: " + perso.getArmePrincipale().getMunitions() + "/" + perso.getArmePrincipale().getMunitionsMax();
        else 
            texte_munitions = " MUNITIONS: ø ";
            
        //  COULEUR
        
        if(perso.getArmePrincipale().getMunitions() != -1 && perso.getArmePrincipale().getMunitions() <= 0)
            couleur_munitions_principales = gris;
        if(!perso.getArmeUnique() && perso.getArmeSecondaire().getMunitions() != -1 && perso.getArmeSecondaire().getMunitions() <= 0)
            couleur_munitions_secondaires = gris;
        if(couleur_munitions_principales == gris && (couleur_munitions_secondaires == gris || perso.getArmeUnique()))
            couleur_armes = gris;
        if(perso.getPV() < 100)
            couleur_pv = rouge;
            
        //  BLIT
        
        police.renderTextBlended(perso.getNom(), blanc).blitSurface(parent, new SDLRect(position.getX(), position.getY()));
        police.renderTextBlended("PV: " + perso.getPV() + "/" + perso.getPVMax(), couleur_pv).blitSurface(parent, new SDLRect(position.getX(), position.getY() + 25));
        police.renderTextBlended("ARMES:", couleur_armes).blitSurface(parent, new SDLRect(position.getX(), position.getY() + 50));
        police.renderTextBlended("[A] " + perso.getArmePrincipale().getNom() + " -> " + texte_munitions + " DÉGATS: " + perso.getArmePrincipale().getDegats(), couleur_munitions_principales).blitSurface(parent, new SDLRect(position.getX() + 20, position.getY() + 75));
        if(!perso.getArmeUnique())
            police.renderTextBlended("[Z] " + texte_arme_secondaire, couleur_munitions_secondaires).blitSurface(parent, new SDLRect(position.getX() + 20, position.getY() + 100));
    }
    
	/**
	 * Applique la gravité aux Personnages
     * @author Quentin KAIFFER
	 * @param perso Le personnage sur lequel appliquer la gravité
	 * @param ecran L'écran pour récupérer la taille de la fenêtre
	 * @param img_sol Le sol pour délimiter la gravité
	*/
    public static void appliquerGravite(Personnage perso, SDLSurface ecran, SDLSurface img_sol) throws SDLException, InterruptedException
    {   
        final int PIXELS_GRAVITE = 50;
        
        if(perso.getPositionY() + perso.getSprite().getHeight() < ecran.getHeight() - img_sol.getHeight() && (!perso.getSaut() || perso.getNiveauSaut() >= perso.getNiveauSautMax()))
        {
            perso.setPositionY(perso.getPositionY() + PIXELS_GRAVITE);
            perso.setNiveauSaut(perso.getNiveauSautMax());
            perso.setSaut(false);
        }
        if(perso.getPositionY() + perso.getSprite().getHeight() >= ecran.getHeight() - img_sol.getHeight())
        {
            perso.setPositionY(ecran.getHeight() - img_sol.getHeight() - perso.getSprite().getHeight());
            perso.setNiveauSaut(0);
        }
    }
    
	/**
	 * Applique la gravité aux Projectiles
     * @author Quentin KAIFFER, Romain MONIER
	 * @param proj Le projectile sur lequel appliquer la gravité
	 * @param ecran L'écran pour récupérer la taille de la fenêtre
	 * @param img_sol Le sol pour délimiter la gravité
	*/
    public static void appliquerGravite(Projectile proj, SDLSurface ecran, SDLSurface img_sol) throws SDLException, InterruptedException
    {   
        final int PIXELS_GRAVITE = 2;
        
        if(proj.getPositionY() + proj.getSprite().getHeight() < ecran.getHeight() - img_sol.getHeight())
        {
            proj.setPositionY(proj.getPositionY() + PIXELS_GRAVITE);
        }
        if(proj.getPositionY() + proj.getSprite().getHeight() >= ecran.getHeight() - img_sol.getHeight())
        {
            proj.setPositionY(ecran.getHeight() - img_sol.getHeight() - proj.getSprite().getHeight());
        }
    }
    
	/**
	 * Tour de l'IA
     * @author Quentin KAIFFER, Romain MONIER
	 * @param perso Le personnage qui doit jouer
	 * @param adversaire Le personnage ennemi
	 * @param ecran L'écran pour récupérer la taille de la fenêtre
	 * @param img_sol Le sol pour délimiter la gravité
	 * @param pos_img_sol La position du sol pour délimiter la gravité
	 * @param onscreen_projectiles Le tableau de projectiles actifs pour pouvoir y ajouter ceux lancés
	 * @param son_munvide Le son à jouer lors du tir
	 * @param ARME_SAMUS_Y Constante pour placer le tir proche de l'arme (peu fonctionnel)
	*/
    public static void actionIA(Personnage perso, Personnage adversaire, SDLSurface ecran, SDLSurface img_sol, SDLRect pos_img_sol, Projectile[] onscreen_projectiles, MixChunk son_munvide, int ARME_SAMUS_Y) throws SDLException, InterruptedException 
    {
        int nouvelle_pos, ancienne_vitesse, proba_mouvement, proba_saut, proba_tir_principal, proba_tir_secondaire, proba_courrir;
        DirProjectile direction_projectile;
        DirSprite mouvement;
        boolean saute = false, tir_principal = false, tir_secondaire = false, courrir = false;
        final int DISTANCE_ALEATOIRE = 500;
        
        /*  PROBABILITES D'ACTION    */
            
        proba_saut = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        if((perso.getSaut() && proba_saut <= 70) || proba_saut <= 20)
            saute = true;
            
        proba_courrir = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        if(proba_courrir <= 50)
            courrir = true;
            
        proba_tir_principal = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        if(proba_tir_principal <= 20)
            tir_principal = true;
            
        proba_tir_secondaire = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        if(proba_tir_secondaire <= 5){
            if(perso.getArmeUnique())
                tir_principal = true;
            else
                tir_secondaire = true;
        }
        
        /*  RECUPERATION POSITION PERSO POUR MISSILE    */
            
        switch(perso.getDirection())
        {
            case DROITE:
                direction_projectile = DirProjectile.DROITE;
                break;
            case GAUCHE:
                direction_projectile = DirProjectile.GAUCHE;
                break;
            default:
                direction_projectile = DirProjectile.DROITE;
                break;
        }
        
        /*  PHASE DE DEPLACEMENT    */ 
        
        ancienne_vitesse = perso.getVitesse();
        if(courrir) {
            // courrir true
            perso.setVitesse((int)perso.getVitesse() * 2);
        }
        else {
            // courrir false
            perso.setVitesse((int)perso.getVitesse() / 2);
        }
        
        if(perso.getPositionX() >= adversaire.getPositionX() + DISTANCE_ALEATOIRE)
            mouvement = DirSprite.GAUCHE;
        else if(perso.getPositionX() < adversaire.getPositionX() - DISTANCE_ALEATOIRE)
            mouvement = DirSprite.DROITE;
        else {
            proba_mouvement = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            if(proba_mouvement <= ThreadLocalRandom.current().nextInt(0, 100 + 1))
                mouvement = DirSprite.DROITE;
            else
                mouvement = DirSprite.GAUCHE;
        }
        
        if(!tir_principal && !tir_secondaire)
        {
            switch(mouvement)
            { 
                case GAUCHE:
                    perso.setDirection(DirSprite.GAUCHE);
                    
                    nouvelle_pos = (perso.getPositionX() - perso.getVitesse());
                    if(isOnTerrain(perso.getSprite(), new SDLRect(nouvelle_pos, perso.getPositionY()), new SDLRect(ecran.getWidth(), pos_img_sol.getY()), new SDLRect(0,0))) {
                        perso.setPositionX(nouvelle_pos);
                    }
                    break;
                    
                case DROITE:
                    perso.setDirection(DirSprite.DROITE);
                    
                    nouvelle_pos = (perso.getPositionX() + perso.getVitesse());
                    if(isOnTerrain(perso.getSprite(), new SDLRect(nouvelle_pos, perso.getPositionY()), new SDLRect(ecran.getWidth(), pos_img_sol.getY()), new SDLRect(0,0))) {
                        perso.setPositionX(nouvelle_pos);
                    }
                    break;
                    
                default:
                    break;
            }
        }
        
        /*  SAUT    */
        
        if(saute)
        {
            if(perso.getNiveauSaut() < perso.getNiveauSautMax())
            {
                perso.setSaut(true);
                perso.sauter();
                perso.setPositionY(perso.getPositionY() - perso.getVitesseSaut());
            } else { perso.setSaut(false); }
        } else { perso.setSaut(false); }
        
        /*  PHASE DE TIR    */
        
        if(!saute && tir_principal && ((perso.getPositionX() >= adversaire.getPositionX() && perso.getDirection() == DirSprite.GAUCHE) || (perso.getPositionX() <= adversaire.getPositionX() && perso.getDirection() == DirSprite.DROITE)) && perso.getDirection() != DirSprite.DEVANT && perso.getDirection() != DirSprite.DERRIERE)
        {
            if(perso.tirerArmePrincipale())
            {
                for(int i = 0 ; i < onscreen_projectiles.length ; i++)
                {
                    if(!(onscreen_projectiles[i] instanceof Projectile) || !(onscreen_projectiles[i].isOnScreen()))
                    {
                        onscreen_projectiles[i] = perso.getArmePrincipale().tirer(direction_projectile);
                        if(Parametres.getSon())
                            SDLMixer.playChannel(-1, onscreen_projectiles[i].getSonTir(), 0);
                                                
                        int position_arme_x = 0;
                        if(perso.getDirection() == DirSprite.DROITE)
                            position_arme_x = perso.getSprite().getWidth();
                        else if(perso.getDirection() == DirSprite.GAUCHE)
                            position_arme_x = -onscreen_projectiles[i].getSprite().getWidth();
                                                
                        onscreen_projectiles[i].setPositionX(perso.getPositionX()+position_arme_x);
                        onscreen_projectiles[i].setPositionY(perso.getPositionY()+ARME_SAMUS_Y+20);
                                                
                        break;
                    }
                } 
            }   
            else 
            {
                if(Parametres.getSon())
                    SDLMixer.playChannel(-1, son_munvide, 0);
            }                                
        }
        else if(!saute && tir_secondaire && ((perso.getPositionX() >= adversaire.getPositionX() && perso.getDirection() == DirSprite.GAUCHE) || (perso.getPositionX() <= adversaire.getPositionX() && perso.getDirection() == DirSprite.DROITE)) && perso.getDirection() != DirSprite.DEVANT && perso.getDirection() != DirSprite.DERRIERE)
        {
            if(perso.tirerArmeSecondaire())
            {
                for(int i = 0 ; i < onscreen_projectiles.length ; i++)
                {
                    if(!(onscreen_projectiles[i] instanceof Projectile) || !(onscreen_projectiles[i].isOnScreen()))
                    {
                        onscreen_projectiles[i] = perso.getArmeSecondaire().tirer(direction_projectile);
                        if(Parametres.getSon())
                            SDLMixer.playChannel(-1, onscreen_projectiles[i].getSonTir(), 0);
                                                
                        int position_arme_x = 0;
                        if(perso.getDirection() == DirSprite.DROITE)
                            position_arme_x = perso.getSprite().getWidth();
                        else if(perso.getDirection() == DirSprite.GAUCHE)
                            position_arme_x = -onscreen_projectiles[i].getSprite().getWidth();
                                                
                        onscreen_projectiles[i].setPositionX(perso.getPositionX()+position_arme_x);
                        onscreen_projectiles[i].setPositionY(perso.getPositionY()+ARME_SAMUS_Y+20);
                                                
                        break;
                    }
                } 
            }   
            else 
            {
                if(Parametres.getSon())
                    SDLMixer.playChannel(-1, son_munvide, 0);
            }                                
        }
        
        perso.setVitesse(ancienne_vitesse);
    }
}
