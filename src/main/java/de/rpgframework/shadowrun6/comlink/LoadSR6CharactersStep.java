package de.rpgframework.shadowrun6.comlink;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.prelle.simplepersist.SerializationException;

import de.rpgframework.ResourceI18N;
import de.rpgframework.character.Attachment;
import de.rpgframework.character.Attachment.Format;
import de.rpgframework.character.Attachment.Type;
import de.rpgframework.character.CharacterHandle;
import de.rpgframework.character.CharacterIOException;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.eden.client.jfx.steps.StartupStep;
import de.rpgframework.shadowrun6.Shadowrun6Character;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import de.rpgframework.shadowrun6.Shadowrun6Tools;

/**
 * @author prelle
 *
 */
public class LoadSR6CharactersStep implements StartupStep {

	public final static ResourceBundle RES = ComLinkMain.RES;

	protected static Logger logger = System	.getLogger(LoadSR6CharactersStep.class.getPackageName());

	private ComLinkMain main;

	//-------------------------------------------------------------------
	/**
	 */
	public LoadSR6CharactersStep(ComLinkMain main) {
		this.main = main;
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			logger.log(Level.INFO, "CharacterProvider {0}", CharacterProviderLoader.getCharacterProvider());
			List<CharacterHandle> handles = CharacterProviderLoader.getCharacterProvider().getMyCharacters(RoleplayingSystem.SHADOWRUN6);
			logger.log(Level.INFO, "Resolving characters");
			for (CharacterHandle handle : handles) {
				Attachment attach = null;
				try {
					try {
						attach = CharacterProviderLoader.getCharacterProvider().getFirstAttachment(handle, Type.CHARACTER, Format.RULESPECIFIC);
					} catch (Exception e) {
						logger.log(Level.ERROR, "Error loading character attachment",e);
						continue;
					}
					if (attach==null) {
						logger.log(Level.WARNING, "No character XML attachment for "+handle.getPath());
						continue;
					}
					Shadowrun6Character parsed = Shadowrun6Core.decode(attach.getData());
					Shadowrun6Tools.resolveChar(parsed);
					logger.log(Level.INFO, "Parsed character1: {1} is {0}", parsed.getName(), handle.getUUID());
					Shadowrun6Tools.runProcessors(parsed, Locale.getDefault());
					handle.setCharacter(parsed);
					handle.setShortDescription(parsed.getShortDescription());
					logger.log(Level.INFO, "Parsed character2: "+handle.getName()+": "+parsed.getShortDescription());
				} catch (CharacterIOException e) {
					if (e.getCause()!=null && e.getCause() instanceof SerializationException) {
						SerializationException cause = (SerializationException) e.getCause();
						logger.log(Level.ERROR, "Failed decoding character {0} in line {2}:{3}\nReason: {1}", handle.getName(),cause.getMessage(), cause.getLine(), cause.getColumn());
						BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, ResourceI18N.format(RES, "error.decoding_character",
								handle.getName(),
								e.getCode(),
								cause.getLine(),
								cause.getColumn(),
								cause.getMessage()
								), cause, (attach!=null)?attach.getLocalFile():null);
					} else {
						logger.log(Level.ERROR, "Failed loading character {0}: {1}", handle.getName(),e.getCode(),e);
						BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, ResourceI18N.format(RES, "error.loading_character", handle.getName(), e.getCode()), e);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.log(Level.ERROR, "Failed loading character {0}", handle.getName(),e);
					e.printStackTrace();
					BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, ResourceI18N.format(RES, "error.loading_character", handle.getName()));
				}
			}
			BabylonEventBus.fireEvent(BabylonEventType.CHAR_MODIFIED, 2);
		} catch (CharacterIOException e) {
			logger.log(Level.ERROR, "Error accessing characters",e);
			main.handleError(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.eden.client.jfx.steps.StartupStep#canRun()
	 */
	@Override
	public boolean canRun() {
		return true;
	}

}
