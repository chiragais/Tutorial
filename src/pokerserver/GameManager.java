package pokerserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pokerserver.cards.Card;
import pokerserver.others.ComareObjects;
import pokerserver.players.GamePlay;
import pokerserver.players.HandManager;
import pokerserver.players.PlayerBean;
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
	HandManager handManager;
	public GamePlay gamePlay;
	ArrayList<Card> listDefaultCards = new ArrayList<Card>();
	RoundManager preflopRound;
	RoundManager flopRound;
	RoundManager turnRound;
	RoundManager riverRound;
	int currentRound = 0;

	public GameManager() {
		// TODO Auto-generated constructor stub
		playersManager = new PlayersManager();
		initGameRounds();
		//playersManager = new PlayersManager();
		// gamePlay=new GamePlay(playersManager);
//		generateDefaultCards();
//		handManager = new HandManager(listDefaultCards);
	}

	public void initGameRounds(){
		System.out.println();
		System.out.print("========//=========//======== Game is init");
		generateDefaultCards();
		
		preflopRound = new RoundManager(ROUND_PREFLOP);
		flopRound = new RoundManager(ROUND_FLOP);
		turnRound = new RoundManager(ROUND_TURN);
		riverRound = new RoundManager(ROUND_RIVER);
		
		handManager = new HandManager(listDefaultCards);
	}
	public void createGamePlat() {
		// gamePlay = new GamePlay(playersManager);
	}

	public void setTableCards() {
		// gamePlay.setTableCards(listDefaultCards);
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

	public void addNewPlayerToGame(PlayerBean player) {
		// handManager.findPlayerBestHand(player.getPlayerCards());
		player.setPlayersBestHand(
				handManager.findPlayerBestHand(player.getPlayerCards()),
				handManager.getPlayerBestCards());
		this.playersManager.addNewPlayerInRoom(player);
	}

	public void leavePlayerToGame(PlayerBean player) {
		this.playersManager.removePlayerFromRoom(player);
	}

	public PlayerBean getPlayerFromPosition(int position) {
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

	// public void setCurrentPlayerId(int curId) {
	// gamePlay.setCurrentPlayerId(curId);
	// }

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

	public PlayerBean getWinnerPlayer() {

		/*
		 * for(Player player : playersManager.getAllAvailablePlayers()){
		 * if(player.isPlayerActive()){ System.out.println();
		 * System.out.print("Winner Player : "
		 * +player.getHandRank()+" <<<<<<<<<<<<"); for(Card card :
		 * player.getBestHandCards()){ System.out.println();
		 * System.out.print("Cards : "+card.getCardName()); } } }
		 */
		Collections.sort(playersManager.getAllAvailablePlayers(),
				new Comparator<PlayerBean>() {
					@Override
					public int compare(PlayerBean paramT1, PlayerBean paramT2) {
						return paramT1.getHandRank().compareTo(
								paramT2.getHandRank());
					}
				});

		PlayerBean winnerPlayer = null;
		for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
			System.out.println();
			System.out.print("Winner Player : " + player.getPlayeName()
					+ " >> Rank >> " + player.getHandRank() + " <<<<<<<<<<<<"
					+ player.getHandRank().ordinal());
			if (player.isPlayerActive() && winnerPlayer == null) {
				winnerPlayer = player;
			}
		}

		return winnerPlayer;
	}

	public ArrayList<String> getWinnerCards() {
		return gamePlay.getWinnerCards();
	}

	public PlayerBean getPlayerByName(String name) {
		for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
			if (player.getPlayeName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	public PlayerBean deductPlayerBetAmountFromBalance(String name, int amount,
			int action) {
		for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
			if (player.getPlayeName().equals(name)) {
				if (action == ACTION_FOLD) {
					player.setPlayerActive(false);
				} else if (player.isPlayerActive()) {
					player.deductBetAmount(amount);
				}
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
		
		ArrayList<PlayerBetBean> totalPlayerWiseBetAmount = new ArrayList<PlayerBetBean>();
		RoundManager currentRound = getCurrentRoundInfo();
		
		for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
			if (player.isPlayerActive()) {
				
				totalPlayerWiseBetAmount.add(new PlayerBetBean(currentRound
						.getTotalPlayerBetAmount(player),currentRound.getPlayerLastAction(player)));
			}
		}
		
//		Collections.sort(totalPlayerWiseBetAmount, new ComareObjects());
		Collections.sort(totalPlayerWiseBetAmount,
				new Comparator<PlayerBetBean>() {
					@Override
					public int compare(PlayerBetBean paramT1, PlayerBetBean paramT2) {
						return Integer.compare(paramT1.getBetAmount(),
								paramT2.getBetAmount());
					}
				});

		boolean allPlayerHaveTurn=true;
		// Checking all players checked
		for (PlayerBetBean c : totalPlayerWiseBetAmount) {
			System.out.println();
			System.out.print("<<<<>>>> All Player turn: " + c.getLastAction());
			if(c.getLastAction() == ACTION_PENDING){
				allPlayerHaveTurn = false;
			}
		}
		PlayerBetBean lastPlayerBetAmt = totalPlayerWiseBetAmount.get(0);
		totalPlayerWiseBetAmount.remove(0);
		// Checking all players have same bet amount
		for (PlayerBetBean currentPlayerBetAmt : totalPlayerWiseBetAmount) {
			if (currentPlayerBetAmt.getBetAmount() != lastPlayerBetAmt.getBetAmount()) {
				return false;
			}
		}
		System.out.println();
		System.out.print("<<<<>>>> All Player turn: " + allPlayerHaveTurn);
		if(!allPlayerHaveTurn){
			return false;
		}
		return true;
	}

	public int getPlayerTotalBetAmount(String name) {
		PlayerBean player = getPlayerByName(name);
		int totalBetAmount = 0;
		totalBetAmount += preflopRound.getTotalPlayerBetAmount(player);
		totalBetAmount += flopRound.getTotalPlayerBetAmount(player);
		totalBetAmount += turnRound.getTotalPlayerBetAmount(player);
		totalBetAmount += riverRound.getTotalPlayerBetAmount(player);

		return totalBetAmount;
	}

	public void updateGamePlay() {
		// gamePlay.update();
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
		listDefaultCards.clear();
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
	public TurnManager managePlayerAction(String userName, int userAction,
			int betAmount) {
		// gamePlay.executePlayerAction(betAmount, userAction);
		return addCurrentActionToTurnManager(userName, betAmount, userAction);
	}

	public int getTotalTableAmount() {
		int totalBetAmount = 0;
		totalBetAmount += preflopRound.getTotalRoundBetAmount();
		totalBetAmount += flopRound.getTotalRoundBetAmount();
		totalBetAmount += turnRound.getTotalRoundBetAmount();
		totalBetAmount += riverRound.getTotalRoundBetAmount();
		System.out.println();
		System.out.print("Total Bet Amount : " + totalBetAmount);
		return totalBetAmount;
	}

	private TurnManager addCurrentActionToTurnManager(String userName, int betAmount,int action) {
		
		TurnManager turnManager=null; 
		PlayerBean currentPlayer = deductPlayerBetAmountFromBalance(userName,betAmount,action);
		if (currentPlayer != null) {
			
			RoundManager currentRoundManger = getCurrentRoundInfo();
			turnManager = new TurnManager(currentPlayer,
					action,
					betAmount);
			currentRoundManger.addTurnRecord(turnManager);
			System.out.println();
			System.out.print("Turn Manager # User: "+currentPlayer.getPlayerName()+" # Action: "+action+" # Bet: "+betAmount+" # Round: "+currentRoundManger.getRound());
		}
		return turnManager;
	}
	// public int getWinnerTotalBalance() {
	// return getPlayerByName(getWinner().getPlayeName()).getTotalBalance();
	// }
	class PlayerBetBean{
		int betAmount= 0;
		int lastAction=ACTION_PENDING;
		public PlayerBetBean(int totalBet,int lastAction){
			this.betAmount = totalBet;
			this.lastAction = lastAction;
		}
		public int getBetAmount(){
			return betAmount;
		}
		public int getLastAction(){
			return lastAction;
		}
	}
}
