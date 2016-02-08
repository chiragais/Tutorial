package pokerserver.room;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import pokerserver.GameManager;
import pokerserver.players.PlayerBean;
import pokerserver.turns.TurnManager;
import pokerserver.utils.GameConstants;

import com.shephertz.app42.server.idomain.BaseTurnRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.ITurnBasedRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;

/**
 * 
 * @author Chirag
 */
public class PokerRoomAdapter extends BaseTurnRoomAdaptor implements
		GameConstants {

	private IZone izone;
	private ITurnBasedRoom gameRoom;
	private byte GAME_STATUS;
	GameManager gameManager;

	public PokerRoomAdapter(IZone izone, ITurnBasedRoom room) {
		this.izone = izone;
		this.gameRoom = room;
		GAME_STATUS = STOPPED;
		this.gameManager = new GameManager();
		System.out.println();
		System.out.print("Room : PokerRoomAdapter : Room : " + room.getName());
	}

	@Override
	public void onTimerTick(long time) {
		/*
		 * A game when room full or we can say max users are equals to joined
		 * users
		 */
		// System.out.println();
		// System.out.print("Room : onTimerTick : Status : "+GAME_STATUS+" >> NoOfUser: "+gameRoom.getJoinedUsers().size());
		if (GAME_STATUS == STOPPED
				&& gameRoom.getJoinedUsers().size() >= MIN_PLAYER_TO_START_GAME) {

			// /distributeCarsToPlayerFromDelear();
			gameManager.initGameRounds();
			gameManager.startPreFlopRound();
			gameRoom.startGame(SERVER_NAME);
			GAME_STATUS = RUNNING;
			gameRoom.BroadcastChat(SERVER_NAME, "Game is stated");
			System.out.println();
			System.out.print("Room : onTimerTick : Start Game : ");
		} else if (GAME_STATUS == RESUMED) {
			GAME_STATUS = RUNNING;
			gameRoom.startGame(SERVER_NAME);
		} else if (GAME_STATUS == RUNNING
				&& gameRoom.getJoinedUsers().size() < MIN_PLAYER_TO_START_GAME) {
			GAME_STATUS = STOPPED;
			gameRoom.stopGame(SERVER_NAME);
		}

	}

	private void broadcastPlayerCardsInfo() {

		for (PlayerBean player : gameManager.getPlayersManager()
				.getAllAvailablePlayers()) {

			JSONObject cardsObject = new JSONObject();

			try {
				cardsObject.put(TAG_PLAYER_NAME, player.getPlayeName());
				cardsObject.put(TAG_CARD_PLAYER_1, player.getFirstCard()
						.getCardName());
				cardsObject.put(TAG_CARD_PLAYER_2, player.getSecondCard()
						.getCardName());
				cardsObject.put(TAG_PLAYER_BALANCE, player.getTotalBalance());

			} catch (JSONException e) {
				e.printStackTrace();
			}
			gameRoom.BroadcastChat(SERVER_NAME, RESPONSE_FOR_PLAYERS_INFO
					+ cardsObject.toString());
		}
	}

	/**
	 * Invoked when a move request is received from the client whose turn it is.
	 * 
	 * By default, the sender will be sent back a success response, the turn
	 * user will be updated to the next user in order of joining and a move
	 * notification will be sent to all the subscribers of the room.
	 * 
	 * @param sender
	 *            the user who has sent the move
	 * @param moveData
	 *            the move data sent by the user
	 * @param result
	 *            use this to override the default behavior
	 */
	public boolean isRoundCompelete = false;

	public void handleMoveRequest(IUser sender, String moveData,
			HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleMoveRequest : Sender User : "
				+ sender.getName() + " : Data : " + moveData);
		int playerAction = 0;

		JSONObject responseJson = null;
		moveData = moveData.replace(REQUEST_FOR_ACTION, "");
		// String[] st = moveData.split("#");
		// moveData = st[1];
		// System.out.print("Room : handleMoveRequest : Sender User : "
		// + sender.getName() + " : After : " + moveData);
		try {
			responseJson = new JSONObject(moveData);
			// gameManager.managePlayerTurnData(responseJson);
			playerAction = responseJson.getInt(TAG_ACTION);
			TurnManager turnManager = gameManager.managePlayerAction(
					sender.getName(), playerAction,
					responseJson.getInt(TAG_BET_AMOUNT));

			if (turnManager != null)
				broadcastPlayerActionDoneToOtherPlayers(turnManager);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (playerAction != ACTION_DEALER
				&& gameManager.checkEveryPlayerHaveSameBetAmount()) {
			// if (gameManager.checkEveryPlayerHaveSameBetAmount()) {
			isRoundCompelete = true;
			if (gameManager.getCurrentRoundInfo().getStatus() == ROUND_STATUS_ACTIVE
					&& gameManager.getCurrentRoundIndex() == ROUND_RIVER) {

				gameManager.getCurrentRoundInfo()
						.setStatus(ROUND_STATUS_FINISH);
				// gameManager.gamePlay.setCurrentRoundIndex(ROUND_RIVER);
				// gameManager.updateGamePlay();
				broadcastRoundCompeleteToAllPlayers();
				broadcastGameCompleteToAllPlayers();
				handleFinishGame(gameManager.getWinnerPlayer().getPlayeName(),
						gameManager.getWinnerCards());
				izone.deleteRoom(gameRoom.getId());
			} else {
				gameManager.moveToNextRound();
				gameManager.updateGamePlay();
				broadcastRoundCompeleteToAllPlayers();
			}
		}

	}

	/**
	 * Invoked when a start game request is received from a client when the room
	 * is in stopped state.
	 * 
	 * By default a success response will be sent back to the client, the game
	 * state will be updated and game started notification is sent to all the
	 * subscribers of the room.
	 * 
	 * @param sender
	 *            the user who has sent the request.
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleStartGameRequest(IUser sender, HandlingResult result) {
		System.out.println();

		System.out.print("Room : handleStartGameRequest : Sender User : "
				+ sender.getName());
	}

	/**
	 * Invoked when a stop game request is received from a client when the room
	 * is in started state.
	 * 
	 * By default a success response will be sent back to the client, the game
	 * state will be updated and game stopped notification is sent to all the
	 * subscribers of the room.
	 * 
	 * @param sender
	 *            the user who has sent the request.
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleStopGameRequest(IUser sender, HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleStopGameRequest : Sender User : "
				+ sender.getName());
	}

	/**
	 * Invoked when the timer expires for the current turn user.
	 * 
	 * By default, the turn user will be updated to the next user in order of
	 * joining and a move notification with empty data will be sent to all the
	 * subscribers of the room.
	 * 
	 * @param turn
	 *            the current turn user whose turn has expired.
	 * @param result
	 *            use this to override the default behavior
	 */
	public void onTurnExpired(IUser turn, HandlingResult result) {
		System.out.println();
		System.out.print("Room : onTurnExpired : Turn User : ");

	}

	/**
	 * Invoked when a user leaves the turn based room.
	 * 
	 * By default, the turn user will be updated to the next user in order of
	 * joining if the user who is leaving was the current turn user and a move
	 * notification with empty data will be sent to all the subscribers of the
	 * room.
	 * 
	 * @param user
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleUserLeavingTurnRoom(IUser user, HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleUserLeavingTurnRoom :  User : "
				+ user.getName());
		gameManager.leavePlayerToGame(gameManager.getPlayerByName(user
				.getName()));
		broadcastBlindPlayerDatas();
		// This will be changed.
		if (GAME_STATUS == RUNNING && gameRoom.getJoinedUsers().size() == 0) {
			System.out.println();
			System.out.print("Room : Game Over ..... ");

			// for (IUser iUser : gameRoom.getJoinedUsers()) {
			// iUser.SendChatNotification(SERVER_NAME, user.getName()
			// + " is left room", gameRoom);
			// }
			gameManager.getPlayersManager().removeAllPlayers();
			// handleFinishGame("Chirag", null);
		}
	}

	/*
	 * This function stop the game and notify the room players about winning
	 * user and his cards.
	 */
	private void handleFinishGame(String winningUser, ArrayList<String> cards) {

		try {
			// JSONObject object = new JSONObject();
			// object.put("win", winningUser);
			// object.put("cards", cards);
			//
			// gameRoom.BroadcastChat(SERVER_NAME, RESULT_GAME_OVER + "#" +
			// object);
			// System.out.println();
			// System.out.print("Game OVer : "+object);
			gameRoom.setAdaptor(null);
			izone.deleteRoom(gameRoom.getId());
			gameRoom.stopGame(SERVER_NAME);
			GAME_STATUS = FINISHED;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Invoked when a chat request is received from the client in the room.
	 * 
	 * By default this will trigger a success response back to the client and
	 * will broadcast a notification message to all the subscribers of the room.
	 * 
	 * 
	 * @param sender
	 *            the user who has sent the request
	 * @param message
	 *            the message that was sent
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleChatRequest(IUser sender, String message,
			HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleChatRequest :  User : "
				+ sender.getName() + " : Message : " + message);
	}

	/**
	 * Invoked when a join request is received by the room and the number of
	 * joined users is less than the maxUsers allowed.
	 * 
	 * By default this will result in a success response sent back the user, the
	 * user will be added to the list of joined users of the room and a user
	 * joined room notification will be sent back to all the subscribed users of
	 * the room.
	 * 
	 * @param user
	 *            the user who has sent the request
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleUserJoinRequest(IUser user, HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleUserJoinRequest :  User : "
				+ user.getName());

		// Handle player request

		sendDefaultCards(user);
		broadcastPlayerCardsInfo();
		broadcastBlindPlayerDatas();

	}

	public void onUserPaused(IUser user) {

	}

	public void onUserResume(IUser user) {

	}

	private void distributeCarsToPlayerFromDelear() {
		int totalPlayerInRoom = gameManager.getPlayersManager()
				.getAllAvailablePlayers().size();
		if (totalPlayerInRoom > 0) {
			for (IUser user : gameRoom.getJoinedUsers()) {
				if (user.getName().equals(
						gameManager.getPlayersManager()
								.getAllAvailablePlayers().get(0)
								.getPlayerName())) {
					user.SendChatNotification(SERVER_NAME,
							RESPONSE_FOR_DESTRIBUTE_CARD, gameRoom);
					return;
				}
			}
		}
	}

	/** Manage default and player hand cards */
	public void sendDefaultCards(IUser user) {

		PlayerBean player = new PlayerBean(
				gameRoom.getJoinedUsers().size() - 1, user.getName());
		// Generate player cards
		player.setCards(gameManager.generatePlayerCards(),
				gameManager.generatePlayerCards());
		gameManager.addNewPlayerToGame(player);
		JSONObject cardsObject = new JSONObject();
		try {
			cardsObject.put(TAG_CARD_FLOP_1,
					gameManager.getDefaultCards().get(INDEX_FLOP_1)
							.getCardName());
			cardsObject.put(TAG_CARD_FLOP_2,
					gameManager.getDefaultCards().get(INDEX_FLOP_2)
							.getCardName());
			cardsObject.put(TAG_CARD_FLOP_3,
					gameManager.getDefaultCards().get(INDEX_FLOP_3)
							.getCardName());
			cardsObject
					.put(TAG_CARD_TURN,
							gameManager.getDefaultCards().get(INDEX_TURN)
									.getCardName());
			cardsObject.put(TAG_CARD_RIVER,
					gameManager.getDefaultCards().get(INDEX_RIVER)
							.getCardName());
			// cardsObject.put(TAG_CARD_PLAYER_1, player.getFirstCard()
			// .getCardName());
			// cardsObject.put(TAG_CARD_PLAYER_2, player.getSecondCard()
			// .getCardName());
			System.out.print("New player cards : " + cardsObject.toString());
			user.SendChatNotification(SERVER_NAME, RESPONSE_FOR_DEFAULT_CARDS
					+ cardsObject.toString(), gameRoom);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// broadcastNewPlayerJoinedToOtherPlayers(player);

	}

	private void broadcastBlindPlayerDatas() {
		JSONObject cardsObject = new JSONObject();
		try {
			if (!gameManager.getPlayersManager().getAllAvailablePlayers()
					.isEmpty()) {
				int totalPlayerInRoom = gameManager.getPlayersManager()
						.getAllAvailablePlayers().size();
				System.out.println();
				System.out.print("Total Players : " + totalPlayerInRoom);

				if (totalPlayerInRoom > 0) {
					cardsObject.put(TAG_PLAYER_DEALER, gameManager
							.getPlayersManager().getAllAvailablePlayers()
							.get(0).getPlayerName());
				} else {
					cardsObject.put(TAG_PLAYER_DEALER, RESPONSE_DATA_SEPRATOR);
				}
				if (totalPlayerInRoom > 1) {
					cardsObject.put(TAG_PLAYER_SMALL_BLIND, gameManager
							.getPlayersManager().getAllAvailablePlayers()
							.get(1).getPlayerName());
				} else {
					cardsObject.put(TAG_PLAYER_SMALL_BLIND,
							RESPONSE_DATA_SEPRATOR);
				}
				if (totalPlayerInRoom > 2) {
					cardsObject.put(TAG_PLAYER_BIG_BLIND, gameManager
							.getPlayersManager().getAllAvailablePlayers()
							.get(2).getPlayerName());
				} else {
					cardsObject.put(TAG_PLAYER_BIG_BLIND,
							RESPONSE_DATA_SEPRATOR);
				}
				System.out.println();
				System.out.print("Blind Player Details : "
						+ cardsObject.toString());
				gameRoom.BroadcastChat(SERVER_NAME, RESPONSE_FOR_BLIEND_PLAYER
						+ cardsObject.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void broadcastRoundCompeleteToAllPlayers() {
		JSONObject cardsObject = new JSONObject();
		try {
			cardsObject.put(TAG_ROUND, gameManager.getCurrentRoundIndex());
			cardsObject
					.put(TAG_TABLE_AMOUNT, gameManager.getTotalTableAmount());
			// cardsObject.put(TAG_ACTION,playerAction);
			// cardsObject.put(TAG_PLAYER, player.getPlayeName());
			System.out.println();
			System.out.print("Round done " + cardsObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		gameRoom.BroadcastChat(SERVER_NAME, RESPONSE_FOR_ROUND_COMPLETE
				+ cardsObject.toString());
	}

	private void broadcastGameCompleteToAllPlayers() {
		JSONObject cardsObject = new JSONObject();
		try {
			PlayerBean winnerPlayer = gameManager.getWinnerPlayer();
			cardsObject.put(TAG_ROUND, gameManager.getCurrentRoundIndex());
			cardsObject
					.put(TAG_TABLE_AMOUNT, gameManager.getTotalTableAmount());
			// cardsObject.put(TAG_WINER_TOTAL_BALENCE,
			// gameManager.getWinnerTotalBalance());
			winnerPlayer.setTotalBalance(winnerPlayer.getTotalBalance()
					+ gameManager.getTotalTableAmount());
			cardsObject.put(TAG_WINNER_TOTAL_BALENCE,
					winnerPlayer.getTotalBalance());
			cardsObject.put(TAG_WINNER_NAME, winnerPlayer.getPlayeName());
			cardsObject.put(TAG_WINNER_RANK, winnerPlayer.getHandRank()
					.ordinal());
			cardsObject.put(TAG_WINNER_BEST_CARDS,
					winnerPlayer.getBestHandCardsName());

			// cardsObject.put(TAG_PLAYER, player.getPlayeName());
			gameRoom.BroadcastChat(SERVER_NAME, RESPONSE_FOR_GAME_COMPLETE
					+ cardsObject.toString());
			System.out.println();
			System.out.print("Winner Player : " + cardsObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void broadcastPlayerActionDoneToOtherPlayers(TurnManager turnManager) {

		JSONObject cardsObject = new JSONObject();
		try {
			// cardsObject.put(TAG_BET_AMOUNT, player.getPlayerBetAmount());
			cardsObject.put(TAG_BET_AMOUNT, turnManager.getBetAmount());
			cardsObject
					.put(TAG_TABLE_AMOUNT, gameManager.getTotalTableAmount());
			cardsObject.put(TAG_ACTION, turnManager.getPlayerAction());
			cardsObject.put(TAG_PLAYER_NAME, turnManager.getPlayer()
					.getPlayeName());
			cardsObject.put(TAG_PLAYER_BALANCE, turnManager.getPlayer()
					.getTotalBalance());
			gameRoom.BroadcastChat(SERVER_NAME, RESPONSE_FOR_ACTION_DONE
					+ cardsObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
