/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerserver;

import java.util.ArrayList;

import pokerserver.cards.Card;
import pokerserver.cards.PlayerCards;
import pokerserver.players.WhoopAssHandManager;
import pokerserver.utils.GameConstants;

import com.shephertz.app42.server.AppWarpServer;

/**
 * @author Chirag
 */

public class Main implements GameConstants {
	static WhoopAssHandManager handManager;
	 static ArrayList<Card> listDefaultCards = new ArrayList<Card>();
	 
	public static void main(String[] args) throws Exception {

/*		listDefaultCards.add(new Card(SUIT_DIAMOND, RANK_FIVE));
		  listDefaultCards.add(new Card(SUIT_DIAMOND, RANK_QUEEN));
		  listDefaultCards.add(new Card(SUIT_DIAMOND, RANK_NINE));
		  listDefaultCards.add(new Card(SUIT_HEART, RANK_QUEEN));
		  listDefaultCards.add(new Card(SUIT_HEART, RANK_KING));
		  listDefaultCards.add(new Card(SUIT_DIAMOND, RANK_KING));

		  PlayerCards playerCards = new PlayerCards(
				  new Card(SUIT_DIAMOND, RANK_NINE), 
				  new Card(SUIT_CLUB, RANK_EIGHT));
		  Card WACard = new Card(SUIT_HEART, RANK_FIVE);

		  handManager = new WhoopAssHandManager(listDefaultCards);
		  System.out.println("Player Hand : "+handManager.findPlayerBestHand(playerCards, WACard, ACTION_WA_UP));*/
		String appconfigPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "AppConfig.json";
		System.out.print("AppConfig : " + appconfigPath);
		boolean started = AppWarpServer.start(new PokerServerExtension(),
				appconfigPath);
		if (!started) {
			System.out.println();
			System.out
					.print("Main : AppWarpServer did not start. See logs for details. ");
			throw new Exception(
					"AppWarpServer did not start. See logs for details.");
		} else {
			System.out.println();
			System.out.print("Main : Server Started ");
		}
	}

}
