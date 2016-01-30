/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerserver;

import pokerserver.utils.GameConstants;

import com.shephertz.app42.server.AppWarpServer;

/**
 * @author Chirag
 */

public class Main implements GameConstants {

//	static List<Card> listRoomCards = new ArrayList<Card>();
//	static GameManager gameManager;
//	static int sbAmount = 10;

	public static void main(String[] args) throws Exception {
		
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
		 
		/*gameManager = new GameManager();
		generateDefaultCards();
		initPlayersOnTable();
		gameStartWithFlopRound();*/
	}

	/**
	 * Send bet amount to server ( Client side ) - User name will be get from
	 * IUser(sender) - Action (Call/Fold/Raise/Bet/Timeout/All In/Check) -
	 * Amount (Bet amount) e.g JSONObject object = new JSONObject();
	 * object.put("top", TOP_CARD); object.put("cards", new
	 * JSONArray(USER_CARD)); theClient.sendMove(object.toString());
	 * 
	 * @throws JSONException
	 */
	/*private static void gameStartWithFlopRound() throws JSONException {

		for (int i = 0; i < 1; i++) {

			// First player turn
			JSONObject response = new JSONObject();
			response.put(TAG_ACTION, ACTION_CALL);
			response.put(TAG_BET_AMOUNT,0);
			response.put(TAG_PLAYER, "Player 1");
			TurnManager lastTurnManager = gameManager
					.managePlayerTurnData(response);
			if (lastTurnManager != null) {
				// Send bet amount to other players
				System.out.println();
				System.out.print("Player Name : "
						+ lastTurnManager.getPlayer().getPlayeName()
						+ " :: Action : " + lastTurnManager.getPlayerAction()
						+ " : Bet : " + lastTurnManager.getBetAmount());
			}
			// After every turn game manage will check that every player have
			// equal amount of bet
			System.out.println();
			System.out.print("Is every player have same bet amount : "
					+ gameManager.checkEveryPlayerHaveSameBetAmount());
			// Second player turn
			JSONObject response1 = new JSONObject();
			response1.put(TAG_ACTION, ACTION_BET);
			response1.put(TAG_BET_AMOUNT, 0);
			response1.put(TAG_PLAYER, "Player 2");
			TurnManager lastTurnManager1 = gameManager
					.managePlayerTurnData(response1);
			if (lastTurnManager1 != null) {
				// Send bet amount to other players
				System.out.println();
				System.out.print("Player Name : "
						+ lastTurnManager1.getPlayer().getPlayeName()
						+ " :: Action : " + lastTurnManager1.getPlayerAction()
						+ " : Bet : " + lastTurnManager1.getBetAmount());
			}
			System.out.println();
			System.out.print("Is every player have same bet amount : "
					+ gameManager.checkEveryPlayerHaveSameBetAmount());
			// 3rd player turn
			JSONObject response2 = new JSONObject();
			response2.put(TAG_ACTION, ACTION_FOLD);
			response2.put(TAG_BET_AMOUNT, 0);
			response2.put(TAG_PLAYER, "Player 3");
			TurnManager lastTurnManager2 = gameManager
					.managePlayerTurnData(response2);
			if (lastTurnManager2 != null) {
				// Send bet amount to other players
				System.out.println();
				System.out.print("Player Name : "
						+ lastTurnManager2.getPlayer().getPlayeName()
						+ " :: Action : " + lastTurnManager2.getPlayerAction()
						+ " : Bet : " + lastTurnManager2.getBetAmount());
			}
			System.out.println();
			System.out.print("Is every player have same bet amount : "
					+ gameManager.checkEveryPlayerHaveSameBetAmount());
			if (gameManager.checkEveryPlayerHaveSameBetAmount()) {
				if (gameManager.getCurrentRoundIndex() == ROUND_RIVER) {
					// Find winner from the existing players
					System.out.println();
					System.out.print("All round is finished............");
				} else {
					gameManager.moveToNextRound();
				}
			}
		}
		// Second round is start
	}*/

	/*private static void initPlayersOnTable() {

		Player player1 = new Player(1, "Player 1");
		player1.setSmallBlind(true);
		Player player2 = new Player(2, "Player 2");
		player2.setBigBlind(true);
		Player player3 = new Player(3, "Player 3");

		player1.setCards(generatePlayerCards(1),
				generatePlayerCards(2));
		player2.setCards(generatePlayerCards(3),
				generatePlayerCards(4));
		player3.setCards(generatePlayerCards(5),
				generatePlayerCards(6));

		gameManager.addNewPlayerToGame(player1);
		gameManager.addNewPlayerToGame(player2);
		gameManager.addNewPlayerToGame(player3);
	}

	public static void generateDefaultCards() {

		while (listRoomCards.size() != 5) {
			Card cardBean = new Card();
			if (!isAlreadyDesributedCard(cardBean)) {
				System.out.println();
				System.out.print("Default card : " + cardBean.getCardName());
				listRoomCards.add(cardBean);
			}
		}
//		generatePlayerCards(1);
//		generatePlayerCards(1);
//		generatePlayerCards(2);
//		generatePlayerCards(2);
//		generatePlayerCards(3);
//		generatePlayerCards(3);
	}

	public static Card generatePlayerCards(int playerIndex) {
		Card cardBean = new Card();
		while (isAlreadyDesributedCard(cardBean)) {
			cardBean.generateRandomCard();
		}
		System.out.println();
		System.out.print("Player " + playerIndex + " card : "
				+ cardBean.getCardName());
		listRoomCards.add(cardBean);
		return cardBean;
	}

	public static boolean isAlreadyDesributedCard(Card cardBean) {
		for (Card cardBean2 : listRoomCards) {
			if (cardBean.getCardName().equals(cardBean2.getCardName())) {
				return true;
			}
		}
		return false;
	}*/

}
