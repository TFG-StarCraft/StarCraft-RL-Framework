package bot;

import com.Com;

import bot.event.Event;

public class BotDestruirUnidad extends Bot {

	public BotDestruirUnidad(Com com) {
		super(com);
	}

	@Override
	public void checkEnd() {
		for (Event event : events) {
			switch (event.getCode()) {
			case Event.CODE_KILL:
				com.onSendMessage("Randy ha matado :)");
				com.ComData.enFinal = true;
				break;
			case Event.CODE_KILLED:
				com.onSendMessage("Randy ha muerto :(");
				com.ComData.enFinal = true;
				break;
			default:
				com.ComData.enFinal = false;
				break;
			}
		}
	}

}