package pokerserver;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

import pokerserver.others.ComareObjects;
import pokerserver.cards.Card;
import pokerserver.players.GamePlay;
import pokerserver.players.Player;
import pokerserver.players.PlayersManager;
import pokerserver.rounds.RoundManager;
import pokerserver.turns.TurnManager;
import pokerserver.utils.GameConstants;

/**
 * Manage player, round and all other tasks
 * 
 * @author Chirag
 * 
 */
public class GameManager implements GameConstants {

	PlayersManager playersManager;
	public GamePlay gamePlay;
	ArrayList<Card> listDefaultCards = new ArrayList<Card>();
	RoundManager preflopRound;
	RoundManager flopRound;
	RoundManager turnRound;
	RoundManager riverRound;
	int currentRound=0;

	public GameManager() {
		// TODO Auto-generated constructor stub
		preflopRound = new RoundManager(ROUND_PREFLOP);
		flopRound = new RoundManager(ROUND_FLOP);
		turnRound = new RoundManager(ROUND_TURN);
		riverRound = new RoundManager(ROUND_RIVER);
		playersManager = new PlayersManager();
		// gamePlay=new GamePlay(playersManager);
		generateDefaultCards();
	}

	public void createGamePlat() {
//		gamePlay = new GamePlay(playersManager);
	}

	public void setTableCards() {
//		gamePlay.setTableCards(listDefaultCards);
	}

	public RoundManager getCurrentRoundInfo() {
		if (preflopRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return preflopRound;
		} else if (flopRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return flopRound;
		} else if (turnRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return turnRound;
		} else if (riverRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return riverRound;
		}
		return null;
	}

	public void addNewPlayerToGame(Player player) {
		this.playersManager.addNewPlayerInRoom(player);
	}

	public void leavePlayerToGame(Player player){
		this.playersManager.removePlayerFromRoom(player);
	}
	public Player getPlayerFromPosition(int position) {
		return this.playersManager.getPlayer(position);
	}

	public PlayersManager getPlayersManager() {
		return playersManager;
	}

	public ArrayList<Card> getDefaultCards() {
		return listDefaultCards;
	}

	public void setPlayersManager(PlayersManager playersManager) {
		this.playersManager = playersManager;
	}

	public int getCurrentRoundIndex() {
		return currentRound;
	}

//	public void setCurrentPlayerId(int curId) {
//		gamePlay.setCurrentPlayerId(curId);
//	}

	/**
	 * Start pre flop round and set other round status as a pending
	 */
	public void startPreFlopRound() {
		currentRound = ROUND_PREFLOP;
		// gamePlay.setCurrentRoundIndex(ROUND_PREFLOP);
		System.out.println();
		System.out.print(">>>>>>>>>>> Preflop Round started");
		preflopRound.setStatus(ROUND_STATUS_ACTIVE);
		flopRound.setStatus(ROUND_STATUS_PENDING);
		turnRound.setStatus(ROUND_STATUS_PENDING);
		riverRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startFlopRound() {
		currentRound = ROUND_FLOP;
		// gamePlay.setCurrentRoundIndex(ROUND_FLOP);
		System.out.println();
		System.out.print(">>>>>>>>>>> Flop Round started  ");
		preflopRound.setStatus(ROUND_STATUS_FINISH);
		flopRound.setStatus(ROUND_STATUS_ACTIVE);
		turnRound.setStatus(ROUND_STATUS_PENDING);
		riverRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startTurnRound() {
		currentRound = ROUND_TURN;
		// gamePlay.setCurrentRoundIndex(ROUND_TURN);
		System.out.println();
		System.out.print(">>>>>>>>>>> Turn Round started  ");
		preflopRound.setStatus(ROUND_STATUS_FINISH);
		flopRound.setStatus(ROUND_STATUS_FINISH);
		turnRound.setStatus(ROUND_STATUS_ACTIVE);
		riverRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startRiverRound() {
		currentRound = ROUND_RIVER;
		// gamePlay.setCurrentRoundIndex(ROUND_RIVER);
		System.out.println();
		System.out.print(">>>>>>>>>>> River Round started  ");
		preflopRound.setStatus(ROUND_STATUS_FINISH);
		flopRound.setStatus(ROUND_STATUS_FINISH);
		turnRound.setStatus(ROUND_STATUS_FINISH);
		riverRound.setStatus(ROUND_STATUS_ACTIVE);
	}

	public RoundManager getPreflopRound() {
		return preflopRound;
	}

	public RoundManager getFlopRound() {
		return flopRound;
	}

	public RoundManager getTurnRound() {
		return turnRound;
	}

	public RoundManager getRiverRound() {
		return riverRound;
	}

	public String getWinner() {
		ArrayList<Integer> winnerList = gamePlay.getWinnerList();
		return String.valueOf(winnerList.get(0));
	}

	public ArrayList<String> getWinnerCards() {
		return gamePlay.getWinnerCards();
	}

	public TurnManager managePlayerTurnData(JSONObject response)
			throws JSONException {
		// First check player is exit in room or not
		TurnManager turnManager = null;
		Player currentPlayer = getPlayerByName(response
				.getString(TAG_PLAYER_NAME));
		if (currentPlayer != null) {
			RoundManager currentRoundManger = getCurrentRoundInfo();
			turnManager = new TurnManager(currentPlayer,
					response.getInt(TAG_ACTION),
					response.getInt(TAG_BET_AMOUNT));
			currentRoundManger.addTurnRecord(turnManager);
		}
		return turnManager;
	}

	public Player getPlayerByName(String name) {
		for (Player player : playersManager.getAllAvailablePlayers()) {
			if (player.getPlayeName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	
	/**
	 * In this function, It will checking all player have equal bet amount on
	 * table. If yes then you have to start new round.
	 * 
	 * @return
	 */
	public boolean checkEveryPlayerHaveSameBetAmount() {

		ArrayList<Integer> totalPlayerWiseBetAmount = new ArrayList<Integer>();
		RoundManager currentRound = getCurrentRoundInfo();

		for (Player player : playersManager.getAllAvailablePlayers()) {
			if (player.getPlayerActive()) {
				totalPlayerWiseBetAmount.add(currentRound
						.getTotalPlayerBetAmount(player));
			}
		}

		Collections.sort(totalPlayerWiseBetAmount, new ComareObjects());
		for (Integer c : totalPlayerWiseBetAmount) {
			System.out.println("card is  " + c);
		}

		int lastPlayerBetAmt = totalPlayerWiseBetAmount.get(0);
		totalPlayerWiseBetAmount.remove(0);
		for (int currentPlayerBetAmt : totalPlayerWiseBetAmount) {
			if (currentPlayerBetAmt != lastPlayerBetAmt) {
				return false;
			}
		}
		return true;
	}

	public void updateGamePlay() {
		//gamePlay.update();
	}

	public void moveToNextRound() {
		switch (currentRound) {
		case ROUND_PREFLOP:
			startFlopRound();
			break;
		case ROUND_FLOP:
			startTurnRound();
			break;
		case ROUND_TURN:
			startRiverRound();
			break;
		case ROUND_RIVER:
			// riverRound.setStatus(ROUND_STATUS_FINISH);
			// gamePlay.setCurrentRoundIndex(ROUND_RIVER);
			break;
		}

	}

	/** It will generate flop(3), turn(1) and river(1) cards. Total 5 cards */
	public void generateDefaultCards() {

		while (listDefaultCards.size() != 5) {
			Card cardBean = new Card();
			if (!isAlreadyDesributedCard(cardBean)) {
				System.out.println();
				System.out.print("Default card : " + cardBean.getCardName());
				listDefaultCards.add(cardBean);
			}
		}

	}

	/** Check card is already on table or not */
	public boolean isAlreadyDesributedCard(Card cardBean) {
		for (Card cardBean2 : listDefaultCards) {
			if (cardBean.getCardName().equals(cardBean2.getCardName())) {
				return true;
			}
		}
		return false;
	}

	public Card generatePlayerCards() {
		Card cardBean = new Card();
		while (isAlreadyDesributedCard(cardBean)) {
			cardBean.generateRandomCard();
		}
		// System.out.print("Default card  : " + cardBean.getCardName());
		listDefaultCards.add(cardBean);
		return cardBean;
	}

	/** Handles player's action taken by him */
	public void managePlayerAction(String userName,int userAction, int betAmount) {
//		gamePlay.executePlayerAction(betAmount, userAction);
		addCurrentActionToTurnManager(userName, betAmount,userAction);
	}

	public int getTotalTableAmount() {
//		return gamePlay.getTotalTableAmount();
		int totalBetAmount = 0;
		if(preflopRound.getStatus()==ROUND_STATUS_FINISH){
			totalBetAmount += preflopRound.getTotalRoundBetAmount();
		}
		if(flopRound.getStatus()==ROUND_STATUS_FINISH){
			totalBetAmount += flopRound.getTotalRoundBetAmount();
		}
		if(turnRound.getStatus()==ROUND_STATUS_FINISH){
			totalBetAmount += turnRound.getTotalRoundBetAmount();
		}
		if(riverRound.getStatus()==ROUND_STATUS_FINISH){
			totalBetAmount += riverRound.getTotalRoundBetAmount();
		}
		return totalBetAmount;
	}

	private void addCurrentActionToTurnManager(String userName, int betAmount,int action) {
		Player currentPlayer = getPlayerByName(userName);
		if (currentPlayer != null) {
			RoundManager currentRoundManger = getCurrentRoundInfo();
			/*if(action==ACTION_FOLD){
				currentPlayer.setPlayerActive(false);
				currentPlayer.setPlayerFolded(true);
				betAmount = 0;
			}else if(action==ACTION_ALL_IN){
//				currentPlayer.setPlayerActive(false);
				currentPlayer.setPlayerAllIn(true);
			}*/
			TurnManager turnManager = new TurnManager(currentPlayer,
					action,
					betAmount);
			currentRoundManger.addTurnRecord(turnManager);
			currentPlayer.deductBetAmount(betAmount);
			System.out.println();
			System.out.print("Turn Manager # User: "+currentPlayer.getPlayerName()+" # Action: "+action+" # Bet: "+betAmount+" # Round: "+currentRoundManger.getRound());
			
			
		}
	}

	public int getWinnerTotalBalance() {
		return getPlayerByName(getWinner()).getTotalBalance();
	}
}
