/* FICHIER METROID.JAVA :
 *      - FICHIER DE DÉMARRAGE / CHARGEMENT DU JEU
 *
 *  DERNIÈRE MÀJ : 28/04/2021 par RM
 *  CRÉÉ PAR ROMAIN MONIER
 *  2016/2017
 * ------------------------------------------
 *  INFOS :
 *      - VOIR README.md AVANT DE COMPILER / EXECUTER !
 * ------------------------------------------
 */

package metroid;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import sdljava.SDLException;

import sdljava.SDLMain;

import sdljava.video.SDLVideo;
import sdljava.video.SDLSurface;
import sdljava.image.SDLImage;

import sdljava.event.SDLEvent;

import sdljava.audio.*;
import sdljava.mixer.*;

import sdljava.ttf.SDLTTF;
import sdljava.ttf.SDLTrueTypeFont;

/** Lancement du menu
 * @author Romain MONIER
 */
public class Metroid
{
    static
    {
        try {
            // manual lib loading, mandatory because some libs depend on others and the OS will check its own Path, not the JVM's
            String lib_path = System.getProperty("user.dir") + "/build/extlibs";
            File lib_directory = new File(lib_path);

            // set the resources path when the env is dev
            System.setProperty("metroid.resources", System.getProperty("user.dir") + "/build/resources/main/");

            boolean inJar = Objects.requireNonNull(Metroid.class.getResource("Metroid.class")).toString().startsWith("jar:");
            if(inJar)
            {
                String tmp_dir = System.getProperty("java.io.tmpdir");
                String temp_path = tmp_dir + (!tmp_dir.substring(tmp_dir.length() - 1).equals(File.separator) ? File.separator : "") + "METROID_RESOURCES_TEMP_" + new Date().getTime();
                lib_path = temp_path + File.separator + "extlibs";
                lib_directory = new File(lib_path);
                File temp_directory = new File(temp_path);
                temp_directory.mkdir();

                // set the resources path when the env is prod because the SDL binding needs real files not JAR URLs
                System.setProperty("metroid.resources", temp_path + File.separator);

                JarFile jar = new JarFile(new File(Metroid.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath().toString());
                Enumeration<JarEntry> enumEntries = jar.entries();
                while (enumEntries.hasMoreElements())
                {
                    JarEntry file = enumEntries.nextElement();
                    File f = new File(temp_path + File.separator + file.getName());

                    // extract the resources
                    if(f.getAbsolutePath().startsWith(lib_path)
                        || f.getAbsolutePath().startsWith(temp_path + File.separator + "audio")
                        || f.getAbsolutePath().startsWith(temp_path + File.separator + "font")
                        || f.getAbsolutePath().startsWith(temp_path + File.separator + "img")
                    ) {
                        if (file.isDirectory()) { // if it's a directory, create it
                            f.mkdir();
                        } else {
                            InputStream is = jar.getInputStream(file); // get the input stream
                            FileUtils.copyInputStreamToFile(is, f); // copy resources to temp dir
                            is.close();
                        }
                    }
                }
                jar.close();
            }

            // handle the lib loading manually (avoid sdljava trying to load the lib from the java.library.path which is not updated there)
            System.setProperty("sdljava.bootclasspath", "");

            List<String> depStack = new ArrayList<>();
            boolean allDepLoaded = true, newLibLoaded = true;

            String[] filters = {".txt", ".jar"}; // filter the non lib files
            File[] files = FileUtils.listFiles(lib_directory, FileFilterUtils.notFileFilter(new SuffixFileFilter(filters)), TrueFileFilter.INSTANCE).toArray(File[]::new);

            for(File f : files) {
                depStack.add(f.getAbsolutePath());
                allDepLoaded = false;
            }
        
            // Load sdl dynamic dependencies
            // While there are deps to load we loop, and if all teh leaving ones are unloadable then we stop
            // mandatory because some lib dependencies have other lib dependencies that we need to load before
            while(!allDepLoaded && newLibLoaded) {
                newLibLoaded = false;
                allDepLoaded = true;
                for(String path : new ArrayList<String>(depStack)) {
                    try {
                        System.load(path);
                        depStack.remove(path);
                        newLibLoaded = true;
                    } catch(Throwable e) {
                        allDepLoaded = false;
                    }
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) throws SDLException, InterruptedException
    {
        //  INITIALISATION SDL
        SDLMain.init(SDLMain.SDL_INIT_VIDEO | SDLMain.SDL_INIT_AUDIO);        
        SDLVideo.wmSetCaption("METROID", null);
        SDLVideo.wmSetIcon(SDLImage.load(System.getProperty("metroid.resources") + "img/logo.png"), (short)0);

        //  INITIALISATION AUDIO
        SDLMixer.openAudio(44100, SDLAudio.AUDIO_S16SYS, 2 , 1024);
        if(SDLMixer.allocateChannels(128) != 128) throw new SDLException("ERREUR: les 128 canaux de mixage n'ont pas pu être alloués !");
        
        SDLEvent.enableUNICODE(1); // On active l'Unicode sur les touches du clavier
        SDLEvent.enableKeyRepeat(1, SDLEvent.SDL_DEFAULT_REPEAT_INTERVAL); // On active la répétion de touche avec un délai de 1ms
        
        SDLTTF.init();
        
        SDLSurface ecran = SDLVideo.setVideoMode(1280, 720, 32, SDLVideo.SDL_HWSURFACE | SDLVideo.SDL_DOUBLEBUF);
        
        MixChunk son_slide = SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/slide.ogg");
        MixChunk son_click = SDLMixer.loadWAV(System.getProperty("metroid.resources") + "audio/snd/click.ogg");
        MixMusic musique = SDLMixer.loadMUS(System.getProperty("metroid.resources") + "audio/msc/home.wav");
        
        if(Parametres.getMusique())
            SDLMixer.playMusic(musique, -1);
        
        Menus.principal(ecran, son_slide, son_click, musique);
        
        if(SDLMixer.playingMusic())
            SDLMixer.haltMusic();
        
        SDLMixer.freeChunk(son_slide);
        SDLMixer.freeChunk(son_click);
		SDLMixer.freeMusic(musique);
        
        ecran.freeSurface();
        
        SDLTTF.quit();        
		SDLMain.quit();
    }
}
