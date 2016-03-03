/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pokerserver.handrank.GeneralHandManager;
import pokerserver.handrank.WhoopAssHandManager;
import pokerserver.players.PlayerBean;
import pokerserver.players.PlayersManager;
import pokerserver.utils.GameConstants;

import com.shephertz.app42.server.AppWarpServer;

/**
 * @author Chirag
 */

public class Main implements GameConstants {
	static WhoopAssHandManager handManager;
	static PlayersManager playersManager ;
	static GeneralHandManager generalHandManager;
	public static void main(String[] args) throws Exception {
/*		generalHandManager = new GeneralHandManager(WA_PLAYER_CARD_LIMIT_FOR_HAND);
		playersManager = new PlayersManager();
		ArrayList<Card> listDefaultCards = new ArrayList<Card>();
		listDefaultCards.add(new Card(SUIT_DIAMOND, RANK_KING));
		listDefaultCards.add(new Card(SUIT_DIAMOND, RANK_QUEEN));
		listDefaultCards.add(new Card(SUIT_DIAMOND, RANK_FOUR));
		listDefaultCards.add(new Card(SUIT_CLUB, RANK_SIX));
		listDefaultCards.add(new Card(SUIT_SPADE, RANK_JACK));
		listDefaultCards.add(new Card(SUIT_CLUB, RANK_FIVE));

		PlayerBean playerBean1 = new PlayerBean(1, "Player 1");
		playerBean1.setCards(new Card(SUIT_CLUB, RANK_TEN), new Card(SUIT_HEART, RANK_KING),  new Card(SUIT_SPADE, RANK_THREE));
		playerBean1.setWACardStatus(ACTION_WA_DOWN);
		playersManager.addNewPlayerInRoom(playerBean1);
		generalHandManager.generatePlayerBestRank(listDefaultCards, playerBean1);
		
		PlayerBean playerBean2 = new PlayerBean(2, "Player 2");
		playerBean2.setCards(new Card(SUIT_DIAMOND, RANK_NINE), new Card(SUIT_SPADE, RANK_TWO),  new Card(SUIT_SPADE, RANK_SIX));
		playerBean2.setWACardStatus(ACTION_WA_DOWN);
		playersManager.addNewPlayerInRoom(playerBean2);
		generalHandManager.generatePlayerBestRank(listDefaultCards, playerBean2);
//		
		PlayerBean playerBean3 = new PlayerBean(2, "Player 3");
		playerBean3.setCards(new Card(SUIT_SPADE, RANK_ACE), new Card(SUIT_CLUB, RANK_ACE),  new Card(SUIT_HEART, RANK_JACK));
		playerBean3.setWACardStatus(ACTION_WA_DOWN);
		playersManager.addNewPlayerInRoom(playerBean3);
		generalHandManager.generatePlayerBestRank(listDefaultCards, playerBean3);
		generateWinnerPlayers();*/
		
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

	public static List<PlayerBean>  generateWinnerPlayers(){
		List<PlayerBean> listWinnerPlayer = new ArrayList<PlayerBean>();
		// Sort winner players
		Collections.sort(playersManager.getAllAvailablePlayers(), new Comparator<PlayerBean>() {
			@Override
			public int compare(PlayerBean player1, PlayerBean player2) {
				return player1.getHandRank().compareTo(player2.getHandRank());
			}
		});
		for (int i =0;i<playersManager.getAllAvailablePlayers().size();i++) {
			List<PlayerBean> sameRankPlayer = new ArrayList<PlayerBean>();
			PlayerBean currentPlayer =playersManager.getAllAvailablePlayers().get(i); 
			if (!listWinnerPlayer.contains(currentPlayer)) {
				sameRankPlayer.add(currentPlayer);
				for (int j = i + 1; j < playersManager.getAllAvailablePlayers()
						.size(); j++) {
					PlayerBean nextPlayer = playersManager
							.getAllAvailablePlayers().get(j);
					if (currentPlayer.getHandRank() == nextPlayer.getHandRank()) {
						sameRankPlayer.add(nextPlayer);
					}
				}
				Collections.sort(sameRankPlayer, new Comparator<PlayerBean>() {
					@Override
					public int compare(PlayerBean player1, PlayerBean player2) {
						return Integer.compare(player2.getBestHandRankTotal(),
								player1.getBestHandRankTotal());
					}
				});
				listWinnerPlayer.addAll(sameRankPlayer);
			}
		}

		for(PlayerBean playerBean : listWinnerPlayer){
			System.out.println("Current Pla : "+playerBean.getPlayeName()+" >> "+playerBean.getBestHandRankTotal()+" >> "+playerBean.getHandRank());
		}
		return listWinnerPlayer;
	}

}
