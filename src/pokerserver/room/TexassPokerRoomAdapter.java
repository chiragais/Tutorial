package pokerserver.room;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pokerserver.TexassGameManager;
import pokerserver.players.PlayerBean;
import pokerserver.players.Winner;
import pokerserver.rounds.RoundManager;
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
public class TexassPokerRoomAdapter extends BaseTurnRoomAdaptor implements
		GameConstants {

	private IZone izone;
	private ITurnBasedRoom gameRoom;
	private byte GAME_STATUS;
	TexassGameManager gameManager;

	List<String> listRestartGameReq = new ArrayList<String>();
	
	public TexassPokerRoomAdapter(IZone izone, ITurnBasedRoom room) {
		this.izone = izone;
		this.gameRoom = room;
		GAME_STATUS = STOPPED;
		this.gameManager = new TexassGameManager();
		gameManager.initGameRounds();
		System.out.println("Texass Room : " + room.getName());
	}

	@Override
	public void onTimerTick(long time) {
		if (GAME_STATUS == STOPPED
				&& gameRoom.getJoinedUsers().size() >= MIN_PLAYER_TO_START_GAME) {
			distributeCarsToPlayerFromDelear();
			GAME_STATUS = RUNNING;
		} else if (GAME_STATUS == RESUMED) {
			GAME_STATUS = RUNNING;
			gameRoom.startGame(TEXASS_SERVER_NAME);
		} else if (GAME_STATUS == RUNNING
				&& gameRoom.getJoinedUsers().size() < MIN_PLAYER_TO_START_GAME) {
			GAME_STATUS = STOPPED;
			gameRoom.stopGame(TEXASS_SERVER_NAME);
		}

	}
	private void startGame() {
		managePlayerTurn(gameManager.getPlayersManager().getBigBlindPayer()
				.getPlayerName());
		gameRoom.startGame(TEXASS_SERVER_NAME);
	}
	private void managePlayerTurn(String currentPlayer) {
		System.out.println(">>Total Players : "
				+ gameRoom.getJoinedUsers().size());
		RoundManager currentRoundManager = gameManager.getCurrentRoundInfo();

		if (currentRoundManager != null) {
			PlayerBean nextPlayer = getNextPlayerFromCurrentPlayer(currentPlayer);
			if (nextPlayer == null) {
				System.out.println(" Next turn player : Null");
			} else {
				while (nextPlayer.isFolded() || nextPlayer.isAllIn()) {
					System.out.println(" Next turn player : "
							+ nextPlayer.getPlayerName());
					nextPlayer = getNextPlayerFromCurrentPlayer(nextPlayer
							.getPlayerName());
				}

				gameRoom.setNextTurn(getUserFromName(nextPlayer.getPlayerName()));
				System.out.println(currentPlayer
						+ " >> Next valid turn player : "
						+ nextPlayer.getPlayerName());
			}
		} else {
			System.out.println("------ Error > Round is not started yet.....");
		}
	}
	public PlayerBean getNextPlayerFromCurrentPlayer(String currentPlayerName) {
		List<PlayerBean> listPlayer = gameManager.getPlayersManager()
				.getAllAactivePlayersForTurn();
		for (int i = 0; i < listPlayer.size(); i++) {
			if (currentPlayerName.equals(listPlayer.get(i).getPlayerName())) {
				if (i == listPlayer.size() - 1) {
					return listPlayer.get(0);
				} else {
					return listPlayer.get(i + 1);
				}
			}
		}
		return null;
	}
	private IUser getUserFromName(String name) {
		for (IUser user : gameRoom.getJoinedUsers()) {
			if (user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}

	private void broadcastPlayerCardsInfo() {

		for (PlayerBean player : gameManager.getPlayersManager()
				.getAllAvailablePlayers()) {
			JSONObject cardsObject = new JSONObject();
			try {
				cardsObject.put(TAG_PLAYER_NAME, player.getPlayerName());
				cardsObject.put(TAG_CARD_PLAYER_1, player.getFirstCard()
						.getCardName());
				cardsObject.put(TAG_CARD_PLAYER_2, player.getSecondCard()
						.getCardName());
				cardsObject.put(TAG_PLAYER_BALANCE, player.getTotalBalance());
				gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_PLAYERS_INFO
						+ cardsObject.toString());
				System.out.println("Texass Player Info : " + cardsObject.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
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
		if (moveData.contains(REQUEST_FOR_ACTION)) {
			System.out.println("\nTexass : MoveRequest : Sender : " + sender.getName()
					+ " : Data : " + moveData);
			int playerAction = 0;
			JSONObject responseJson = null;
			moveData = moveData.replace(REQUEST_FOR_ACTION, "");

			try {
				responseJson = new JSONObject(moveData);
				playerAction = responseJson.getInt(TAG_ACTION);
				if (playerAction != ACTION_NO_TURN) {
					managePlayerAction(sender.getName(), playerAction,
							responseJson.getInt(TAG_BET_AMOUNT));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void manageGameFinishEvent() {
		
		gameManager.moveToNextRound();
		// Broad cast game completed to all players
		broadcastRoundCompeleteToAllPlayers();
		broadcastGameCompleteToAllPlayers();
		gameManager.findBestPlayerHand();
		gameManager.findAllWinnerPlayers();
		broadcastWinningPlayer();
		handleFinishGame();
	}
	public void managePlayerAction(String sender, int playerAction,
			int betAmount) {
		TurnManager turnManager = gameManager.managePlayerAction(sender,
				playerAction, betAmount);

		if (turnManager != null)
			broadcastPlayerActionDoneToOtherPlayers(turnManager);
		// If all players are folded or all in then declare last player as a
		// winner
		PlayerBean lastActivePlayer = gameManager.checkAllAreFoldOrAllIn();
		if (lastActivePlayer != null) {
			manageGameFinishEvent();
		} else if (playerAction != ACTION_DEALER
				&& gameManager.checkEveryPlayerHaveSameBetAmount()) {
			isRoundCompelete = true;
			if (gameManager.getCurrentRoundInfo().getStatus() == ROUND_STATUS_ACTIVE
					&& gameManager.getCurrentRoundIndex() == TEXASS_ROUND_RIVER) {
				manageGameFinishEvent();
			} else {
				gameManager.moveToNextRound();
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
		System.out.println("Room : handleStartGameRequest : Sender User : "
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
		
		System.out.println("Room : handleStopGameRequest : Sender User : "
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
	@Override
	public void handleTurnExpired(IUser turn, HandlingResult result) {
		System.out.println("onTurnExpired : Turn User : " + turn.getName());
		managePlayerAction(turn.getName(), ACTION_FOLD, 0);

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
		
		System.out.println("Room : handleUserLeavingTurnRoom :  User : "
				+ user.getName());
		gameManager.leavePlayerToGame(gameManager.getPlayersManager().getPlayerByName(user
				.getName()));
		broadcastBlindPlayerDatas();
		// This will be changed.
		if (GAME_STATUS == RUNNING || GAME_STATUS == FINISHED
				&& gameRoom.getJoinedUsers().size() == 0) {
			System.out.println("Room : Game Over ..... ");
			gameManager.getPlayersManager().removeAllPlayers();
			GAME_STATUS = FINISHED;
		}
	}

	/*
	 * This function stop the game and notify the room players about winning
	 * user and his cards.
	 */
	private void handleFinishGame() {

		try {
//			gameRoom.setAdaptor(null);
//			izone.deleteRoom(gameRoom.getId());
			gameRoom.stopGame(TEXASS_SERVER_NAME);
			GAME_STATUS = FINISHED;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void handleRestartGame() {

		System.out.println("--- Restarting Game -------- ");
		listRestartGameReq.clear();
		gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_GAME_START);
		gameManager.initGameRounds();
		gameManager.getPlayersManager().removeAllPlayers();
		for (IUser user : gameRoom.getJoinedUsers()) {
			addNewPlayerCards(user.getName());
		}
		sendDefaultCards(null, true);
		broadcastPlayerCardsInfo();
		broadcastBlindPlayerDatas();
		GAME_STATUS = STOPPED;
		System.out.println("Game Status : " + GAME_STATUS);
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
		System.out.println("ChatRequest :  User : " + sender.getName()
				+ " : Message : " + message);
		if (gameManager.getPlayersManager().getDealerPayer().getPlayerName()
				.equals(sender.getName())
				&& message.startsWith(RESPONSE_FOR_DESTRIBUTE_CARD)) {
			System.out.println("Start Game");
			gameManager.startPreFlopRound();
			gameManager
					.managePlayerAction(gameManager.getPlayersManager()
							.getSmallBlindPayer().getPlayerName(), ACTION_BET,
							SBAmount);
			gameManager.managePlayerAction(gameManager.getPlayersManager()
					.getBigBlindPayer().getPlayerName(), ACTION_BET,
					SBAmount * 2);
			startGame();
		} else if (message.startsWith(REQUEST_FOR_RESTART_GAME)) {
			listRestartGameReq.add(sender.getName());
			if (isRequestForRestartFromAllActivePlayers())
				handleRestartGame();
		}
	}
	private boolean isRequestForRestartFromAllActivePlayers() {
		// TODO Auto-generated method stub
		for (PlayerBean playerBean : gameManager.getPlayersManager()
				.getAllAvailablePlayers()) {
			if (!listRestartGameReq.contains(playerBean.getPlayerName())) {
				return false;
			}
		}
		return true;
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
		System.out.println(">>UserJoinRequest :  User : " + user.getName());
		// Handle player request
		if (gameRoom.getJoinedUsers().isEmpty()) {
			GAME_STATUS = STOPPED;
			gameManager.initGameRounds();
		}
		addNewPlayerCards(user.getName());
		sendDefaultCards(user, false);
		broadcastPlayerCardsInfo();
		broadcastBlindPlayerDatas();
		System.out.println("Game Status : " + GAME_STATUS);
	}
	
	private void addNewPlayerCards(String userName) {
		PlayerBean player = new PlayerBean(
				gameRoom.getJoinedUsers().size() - 1, userName);
//		if (gameRoom.getJoinedUsers().size() == 0) {
//			player.setTotalBalance(100);
//		} else if (gameRoom.getJoinedUsers().size() == 1) {
//			player.setTotalBalance(200);
//		} else if (gameRoom.getJoinedUsers().size() == 2) {
//			player.setTotalBalance(400);
//		}
		int totalPlayers =gameManager.getPlayersManager().getAllAvailablePlayers().size() ; 
		if(totalPlayers== 0){
			player.setTotalBalance(2000);
		} else if (totalPlayers== 1) {
			player.setTotalBalance(1000);
		} else if (totalPlayers == 2) {
			player.setTotalBalance(3000);
		}

		player.setCards(gameManager.generatePlayerCards(),
				gameManager.generatePlayerCards(),
				gameManager.generatePlayerCards());

		gameManager.addNewPlayerToGame(player);
	}
	public void onUserPaused(IUser user) {

	}

	public void onUserResume(IUser user) {

	}

	private void distributeCarsToPlayerFromDelear() {
		gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_DESTRIBUTE_CARD);
		System.out.println("Distribute cards...");
	}

	/** Manage default and player hand cards */
	public void sendDefaultCards(IUser user,boolean isBroadcast) {
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
			if (isBroadcast) {
				gameRoom.BroadcastChat(TEXASS_SERVER_NAME,
						RESPONSE_FOR_DEFAULT_CARDS + cardsObject.toString());
			}else{
				user.SendChatNotification(TEXASS_SERVER_NAME, RESPONSE_FOR_DEFAULT_CARDS
						+ cardsObject.toString(), gameRoom);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void broadcastBlindPlayerDatas() {
		JSONObject cardsObject = new JSONObject();
		try {
			if (!gameManager.getPlayersManager().getAllAvailablePlayers()
					.isEmpty()) {
				int totalPlayerInRoom = gameManager.getPlayersManager()
						.getAllAvailablePlayers().size();
				
				System.out.println("Total Players : " + totalPlayerInRoom);

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
				cardsObject.put(TAG_SMALL_BLIEND_AMOUNT,SBAmount);
				
				System.out.println("Blind Player Details : "
						+ cardsObject.toString());
				gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_BLIEND_PLAYER
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
			System.out.println("Round done " + cardsObject.toString());
			gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_ROUND_COMPLETE
					+ cardsObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	private void broadcastWinningPlayer() {
		JSONArray winnerArray = new JSONArray();
		try {
			for (Winner winnerPlayer : gameManager.getAllWinnerPlayers()) {
				// Winner winnerPlayer = gameManager.getTopWinner();
				JSONObject winnerObject = new JSONObject();
				winnerObject.put(TAG_ROUND, gameManager.getCurrentRoundIndex());
				winnerObject.put(TAG_TABLE_AMOUNT,
						gameManager.getTotalTableAmount());

				winnerObject.put(TAG_WINNER_TOTAL_BALENCE, winnerPlayer
						.getPlayer().getTotalBalance());
				winnerObject.put(TAG_WINNER_NAME, winnerPlayer.getPlayer()
						.getPlayerName());
				winnerObject.put(TAG_WINNER_RANK, winnerPlayer.getPlayer()
						.getHandRank().ordinal());
				winnerObject.put(TAG_WINNERS_WINNING_AMOUNT,
						winnerPlayer.getWinningAmount());

				winnerObject.put(TAG_WINNER_BEST_CARDS, winnerPlayer
						.getPlayer().getBestHandCardsName());
				winnerArray.put(winnerObject);
			}

			gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_WINNIER_INFO
					+ winnerArray.toString());
			System.out.println("<<>> " + winnerArray.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void broadcastGameCompleteToAllPlayers() {
        JSONArray   winnerArray=new JSONArray();
        gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_GAME_COMPLETE
		     + winnerArray.toString());
	}

	private void broadcastPlayerActionDoneToOtherPlayers(TurnManager turnManager) {

		JSONObject cardsObject = new JSONObject();
		try {
			cardsObject.put(TAG_BET_AMOUNT, turnManager.getBetAmount());
			cardsObject
					.put(TAG_TABLE_AMOUNT, gameManager.getTotalTableAmount());
			cardsObject.put(TAG_ACTION, turnManager.getPlayerAction());
			cardsObject.put(TAG_PLAYER_NAME, turnManager.getPlayer()
					.getPlayerName());
			cardsObject.put(TAG_PLAYER_BALANCE, turnManager.getPlayer()
					.getTotalBalance());
			gameRoom.BroadcastChat(TEXASS_SERVER_NAME, RESPONSE_FOR_ACTION_DONE
					+ cardsObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
