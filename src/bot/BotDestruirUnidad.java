package bot;

import com.Com;

import bot.event.Event;

public class BotDestruirUnidad extends Bot {

	public BotDestruirUnidad(Com com) {
		super(com);
	}

	@Override
	public boolean checkEnd() {
		boolean r = false;
		for (Event event : events) {
			switch (event.getCode()) {
			case Event.CODE_KILL:
				System.out.println("EVENT KILL " + frames);
				
				com.onSendMessage("Randy ha matado :)");
				com.ComData.onFinal = true;
				
				com.Sync.signalActionEnded();
				
				r = true;
				break;
			case Event.CODE_KILLED:
				com.onSendMessage("Randy ha muerto :(");
				com.ComData.onFinal = true;

				com.Sync.signalActionEnded();
				
				r = true;
				break;
			default:
			//	com.ComData.onFinal = false;
				break;
			}
		}
		
		return r;
	}

}
