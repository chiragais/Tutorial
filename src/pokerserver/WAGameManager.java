package pokerserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pokerserver.cards.Card;
import pokerserver.players.AllInPlayer;
import pokerserver.players.GamePlay;
import pokerserver.players.HandManager;
import pokerserver.players.PlayerBean;
import pokerserver.players.PlayersManager;
import pokerserver.players.WhoopAssHandManager;
import pokerserver.players.Winner;
import pokerserver.players.WinnerManager;
import pokerserver.rounds.RoundManager;
import pokerserver.turns.TurnManager;
import pokerserver.utils.GameConstants;

/**
 * Manage player, round and all other tasks
 * 
 * @author Chirag
 * 
 */
public class WAGameManager implements GameConstants {

	PlayersManager playersManager;
	WhoopAssHandManager handManager;
	public GamePlay gamePlay;
	ArrayList<Card> listDefaultCards = new ArrayList<Card>();
	ArrayList<Card> listAllTableCards = new ArrayList<Card>();
	RoundManager startRound;
	RoundManager firstFlopRound;
	RoundManager secondFlopRound;
	RoundManager whoopAssRound;
	RoundManager thirdRound;
	int currentRound = 0;
	WinnerManager winnerManager;
	public WAGameManager() {
		// TODO Auto-generated constructor stub
		playersManager = new PlayersManager();
	//	initGameRounds();
	}

	public void initGameRounds() {
		System.out
				.println("================== WA Game started ==================");
		winnerManager = new WinnerManager(playersManager);
		generateDefaultCards();
		startRound = new RoundManager(WA_ROUND_START);
		firstFlopRound = new RoundManager(WA_ROUND_FIRST_FLOP);
		secondFlopRound = new RoundManager(WA_ROUND_SECOND_FLOP);
		whoopAssRound = new RoundManager(WA_ROUND_WHOOPASS);
		thirdRound = new RoundManager(WA_ROUND_THIRD_FLOP);
		handManager = new WhoopAssHandManager(listDefaultCards);
		startFirstRound();
	}

	public void createGamePlat() {
		// gamePlay = new GamePlay(playersManager);
	}

	public void setTableCards() {
		// gamePlay.setTableCards(listDefaultCards);
	}

	public RoundManager getCurrentRoundInfo() {
		if (startRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return startRound;
		} else if (firstFlopRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return firstFlopRound;
		} else if (secondFlopRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return secondFlopRound;
		} else if (whoopAssRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return whoopAssRound;
		} else if (thirdRound.getStatus() == ROUND_STATUS_ACTIVE) {
			return thirdRound;
		}
		return null;
	}

	public void addNewPlayerToGame(PlayerBean player) {
		
//		player.setPlayersBestHand(
//				handManager.findPlayerBestHand(player.getPlayerCards(),player.getWACard(),GameConstants.ACTION_WA_UP),
//				handManager.getPlayerBestCards());
		this.playersManager.addNewPlayerInRoom(player);
	}

	public void findBestPlayerHand() {
		for(PlayerBean player : playersManager.getAllAvailablePlayers()){
		HAND_RANK rank=	handManager.findPlayerBestHand(player.getPlayerCards(),player.getWACard(),player.getWACardStatus());
    	player.setPlayersBestHand(rank,handManager.getPlayerBestCards());
		}
		
	}
	
	
	
//	for (Card card : player.getBestHandCards()) {
//	System.out.println("Player Name is  =  "+player.getPlayeName() );
//	System.out.print("Player Best Cards : " + card.getCardName());
//}
	
//	public Winner getTopWinner() {
//		return winnerManager.getTopWinner();
//	}
	
	public PlayerBean getTopWinner() {
		return playersManager.getAllAvailablePlayers().get(0);
	}
	
	public ArrayList<String> getAllWinnerName() {
		ArrayList<String> listWinners = new ArrayList<String>();
		for (Winner winner : winnerManager.getWinnerList()) {
			listWinners.add(winner.getPlayer().getPlayeName());
		}
		return listWinners;
	}

	public ArrayList<Winner> getAllWinnerPlayers() {
		return winnerManager.getWinnerList();
	}

	public void findAllWinnerPlayers() {
		winnerManager.findWinnerPlayers();
	}

	public void calculatePotAmountForAllInMembers() {
		int allInBetTotalAmount = 0;
	
		for (int i = 0; i < playersManager.getAllAvailablePlayers().size(); i++) {
		    allInBetTotalAmount=0;
			PlayerBean player = playersManager.getAllAvailablePlayers().get(i);
			boolean isAllIn = player.isPlayrAllIn();
			int lastAction = getCurrentRoundInfo().getPlayerLastAction(player);
			if (isAllIn
					&& winnerManager.getAllInPotAmount(player.getPlayeName()) == 0) {

				int allInBetAmt = getCurrentRoundInfo()
						.getPlayerBetAmountAtActionAllIn(player);
				for (int j = 0; j < playersManager.getAllAvailablePlayers()
						.size(); j++) {
					if (allInBetAmt < getCurrentRoundInfo()
							.getTotalPlayerBetAmount(
									playersManager.getAllAvailablePlayers()
											.get(j))) {
						// allInAmountArray[i] = allInAmountArray[i] +
						// allInBetAmt;
						allInBetTotalAmount = allInBetTotalAmount + allInBetAmt;

					} else {
						allInBetTotalAmount = allInBetTotalAmount
								+ getCurrentRoundInfo()
										.getTotalPlayerBetAmount(
												playersManager
														.getAllAvailablePlayers()
														.get(j));

					}
				}

				if (startRound.getRound() < getCurrentRoundInfo().getRound())
					allInBetTotalAmount += startRound
							.getTotalRoundBetAmount();
				if (firstFlopRound.getRound() < getCurrentRoundInfo().getRound())
					allInBetTotalAmount += firstFlopRound.getTotalRoundBetAmount();
				if (secondFlopRound.getRound() < getCurrentRoundInfo().getRound())
					allInBetTotalAmount += secondFlopRound.getTotalRoundBetAmount();
				AllInPlayer allInPlayer = new AllInPlayer(
						player.getPlayeName(), allInBetTotalAmount);
				System.out.println("\n All In Player: "+allInPlayer.getPlayeName()+"  is =  "+allInPlayer.getTotalAllInPotAmount());
				winnerManager.addAllInTotalPotAmount(allInPlayer);
			}

		}

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

	/**
	 * Start first round and set other round status as a pending
	 */
	public void startFirstRound() {
		currentRound = WA_ROUND_START;
		System.out.println(">>>>>>>>>>> WA start Round started");
		startRound.setStatus(ROUND_STATUS_ACTIVE);
		firstFlopRound.setStatus(ROUND_STATUS_PENDING);
		secondFlopRound.setStatus(ROUND_STATUS_PENDING);
		whoopAssRound.setStatus(ROUND_STATUS_PENDING);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startFirstFlopRound() {
		currentRound = WA_ROUND_FIRST_FLOP;
		System.out.println(">>>>>>>>>>>WA First Flop Round started  ");
		startRound.setStatus(ROUND_STATUS_FINISH);
		firstFlopRound.setStatus(ROUND_STATUS_ACTIVE);
		secondFlopRound.setStatus(ROUND_STATUS_PENDING);
		whoopAssRound.setStatus(ROUND_STATUS_PENDING);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startSecondFlopRound() {
		currentRound = WA_ROUND_SECOND_FLOP;
		System.out.println(">>>>>>>>>>> WA Second flop Round started  ");
		startRound.setStatus(ROUND_STATUS_FINISH);
		firstFlopRound.setStatus(ROUND_STATUS_FINISH);
		secondFlopRound.setStatus(ROUND_STATUS_ACTIVE);
		whoopAssRound.setStatus(ROUND_STATUS_PENDING);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startWhoopAssRound() {
		currentRound = WA_ROUND_WHOOPASS;
		System.out.println(">>>>>>>>>>> WA WhoopAss Round started  ");
		startRound.setStatus(ROUND_STATUS_FINISH);
		firstFlopRound.setStatus(ROUND_STATUS_FINISH);
		secondFlopRound.setStatus(ROUND_STATUS_FINISH);
		whoopAssRound.setStatus(ROUND_STATUS_ACTIVE);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startThirdFlopRound() {
		currentRound = WA_ROUND_THIRD_FLOP;
		System.out.println(">>>>>>>>>>> WA Third Round started  ");
		startRound.setStatus(ROUND_STATUS_FINISH);
		firstFlopRound.setStatus(ROUND_STATUS_FINISH);
		secondFlopRound.setStatus(ROUND_STATUS_FINISH);
		whoopAssRound.setStatus(ROUND_STATUS_FINISH);
		thirdRound.setStatus(ROUND_STATUS_ACTIVE);
	}

	public RoundManager getStartRound() {
		return startRound;
	}

	public RoundManager getFirstFlopRound() {
		return firstFlopRound;
	}

	public RoundManager getSecondFlopRound() {
		return secondFlopRound;
	}

	public RoundManager getWhoopAssRound() {
		return whoopAssRound;
	}

	public RoundManager getThirdRound() {
		return thirdRound;
	}
	
	
	public PlayerBean getWinnerPlayer() {

//		Collections.sort(playersManager.getAllAvailablePlayers(),
//				new Comparator<PlayerBean>() {
//					@Override
//					public int compare(PlayerBean paramT1, PlayerBean paramT2) {
//						return paramT1.getHandRank().compareTo(
//								paramT2.getHandRank());
//					}
//				});
//
//		PlayerBean winnerPlayer = null;
//		for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
//			if (player.isPlayerActive() && winnerPlayer == null) {
//				winnerPlayer = player;
//			}
//		}
//
//		return winnerPlayer;
		return winnerManager.getTopWinner();
	}
	

//	public PlayerBean getWinnerPlayer() {
//
//		Collections.sort(playersManager.getAllAvailablePlayers(),
//				new Comparator<PlayerBean>() {
//					@Override
//					public int compare(PlayerBean paramT1, PlayerBean paramT2) {
//						return paramT1.getHandRank().compareTo(
//								paramT2.getHandRank());
//					}
//				});
//
//		
//		  for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
//				
//				System.out.println("\n winner are =     "+player.getHandRank());   
//			}
//	        System.out.println("\n ---------------------------------");   
//	        findSameRankWinners();
//	        
//	  for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
//				
//				System.out.println("\n winner are =     "+player.getHandRank());   
//			}
//	        
//	        System.out.println("\n ---------------------------------");   
//		
//		
//		
//		PlayerBean winnerPlayer = null;
//		for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
//			if (player.isPlayerActive() && winnerPlayer == null) {
//				winnerPlayer = player;
//			}
//		}
//  
//		return	playersManager.getAllAvailablePlayers().get(0);
//		
//		//return winnerPlayer;
//	}
	
	
	 public void findSameRankWinners(){
		   
		   
		   ArrayList<PlayerBean> listPlayers = new ArrayList<PlayerBean>();
		   listPlayers.clear();
			for (int i = 0; i < playersManager.getAllAvailablePlayers().size(); i++) {
				listPlayers.add(playersManager.getAllAvailablePlayers().get(i));
			}
		  PlayerBean player=playersManager.getAllAvailablePlayers().get(0);
		  for(PlayerBean playerTemp:listPlayers){
			  if(player.getHandRank()!=playerTemp.getHandRank()){
				  playersManager.getAllAvailablePlayers().remove(playerTemp);
			  }
			  
		  }
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
				
				if (action == ACTION_ALL_IN) {
					player.setPlayerAllIn(true);
				}
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

		if (currentRound.getRound() == WA_ROUND_WHOOPASS) {
			if(currentRound.getAllTurnRecords().size()==playersManager.getAllAvailablePlayers().size()){
				for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
					if (player.isPlayerActive()) {
						player.setWACardStatus(currentRound.getPlayerLastAction(player));
						System.out.println("\n player last action is  "+currentRound.getPlayerLastAction(player));
					}
				}
				return true;
			}
			return false;
		} else {

			for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
				if (player.isPlayerActive() && !player.isPlayrAllIn()) {
					totalPlayerWiseBetAmount.add(new PlayerBetBean(currentRound
							.getTotalPlayerBetAmount(player), currentRound
							.getPlayerLastAction(player)));
				}
			}

			Collections.sort(totalPlayerWiseBetAmount,
					new Comparator<PlayerBetBean>() {
						@Override
						public int compare(PlayerBetBean paramT1,
								PlayerBetBean paramT2) {
							return Integer.compare(paramT1.getBetAmount(),
									paramT2.getBetAmount());
						}
					});

			boolean allPlayerHaveTurn = true;
			// Checking all players checked
			for (PlayerBetBean c : totalPlayerWiseBetAmount) {
				if (c.getLastAction() == ACTION_PENDING) {
					allPlayerHaveTurn = false;
				}
			}
			PlayerBetBean lastPlayerBetAmt = totalPlayerWiseBetAmount.get(0);
			totalPlayerWiseBetAmount.remove(0);
			// Checking all players have same bet amount
			for (PlayerBetBean currentPlayerBetAmt : totalPlayerWiseBetAmount) {
				if (currentPlayerBetAmt.getBetAmount() != lastPlayerBetAmt
						.getBetAmount()) {
					return false;
				}
			}
			if (!allPlayerHaveTurn) {
				return false;
			}
			return true;

		}
	}

	
	public void setWhoopAssCardStatus(){
		
	}
	
	
	
	public int getPlayerTotalBetAmount(String name) {
		PlayerBean player = getPlayerByName(name);
		int totalBetAmount = 0;
		totalBetAmount += startRound.getTotalPlayerBetAmount(player);
		totalBetAmount += firstFlopRound.getTotalPlayerBetAmount(player);
		totalBetAmount += secondFlopRound.getTotalPlayerBetAmount(player);
		totalBetAmount += thirdRound.getTotalPlayerBetAmount(player);

		return totalBetAmount;
	}

	public void moveToNextRound() {
		switch (currentRound) {
		case WA_ROUND_START:
			calculatePotAmountForAllInMembers();
			startFirstFlopRound();
			break;
		case WA_ROUND_FIRST_FLOP:
		    calculatePotAmountForAllInMembers();
			startSecondFlopRound();
			break;
		case WA_ROUND_SECOND_FLOP:
			calculatePotAmountForAllInMembers();
			startWhoopAssRound();
			break;
		case WA_ROUND_WHOOPASS:
			calculatePotAmountForAllInMembers();
			startThirdFlopRound();
			break;
		case WA_ROUND_THIRD_FLOP:
			// startThirdFlopRound();
			break;
		}

	}

	/** It will generate 1st flop(2), 2nd(2) and 3rd(2) cards. Total 6 cards */
	public void generateDefaultCards() {
		listDefaultCards.clear();
		listAllTableCards.clear();
		while (listDefaultCards.size() != 6) {
			Card cardBean = new Card();
			if (!isAlreadyDesributedCard(cardBean)) {
				System.out.println("Default card : " + cardBean.getCardName());
				listDefaultCards.add(cardBean);
				
			}
		}
	}

	/** Check card is already on table or not */
	public boolean isAlreadyDesributedCard(Card cardBean) {
		for (Card cardBean2 : listAllTableCards) {
			if (cardBean.getCardName().equals(cardBean2.getCardName())) {
				return true;
			}
		}
		listAllTableCards.add(cardBean);
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

	public void setTotalTableBetAmount() {
		int totalBetAmount = 0;
		totalBetAmount += startRound.getTotalRoundBetAmount();
		totalBetAmount += firstFlopRound.getTotalRoundBetAmount();
		totalBetAmount += secondFlopRound.getTotalRoundBetAmount();
		totalBetAmount += thirdRound.getTotalRoundBetAmount();
		winnerManager.setTotalTableAmount(totalBetAmount);
	}
	
	public int getTotalTableAmount() {
		int totalBetAmount = 0;
		totalBetAmount += startRound.getTotalRoundBetAmount();
		totalBetAmount += firstFlopRound.getTotalRoundBetAmount();
		totalBetAmount += secondFlopRound.getTotalRoundBetAmount();
		totalBetAmount += thirdRound.getTotalRoundBetAmount();
		
		winnerManager.setTotalTableAmount(totalBetAmount);
		System.out.println("Total Bet Amount : " + totalBetAmount);
		return totalBetAmount;
	}

	private TurnManager addCurrentActionToTurnManager(String userName,
			int betAmount, int action) {

		TurnManager turnManager = null;
		PlayerBean currentPlayer = deductPlayerBetAmountFromBalance(userName,
				betAmount, action);
		if (currentPlayer != null) {

			RoundManager currentRoundManger = getCurrentRoundInfo();
			turnManager = new TurnManager(currentPlayer, action, betAmount);
			currentRoundManger.addTurnRecord(turnManager);
			System.out.println("TurnManager # User: "
					+ currentPlayer.getPlayerName() + " # Action: " + action
					+ " # Bet: " + betAmount + " # Round: "
					+ currentRoundManger.getRound());
		}
		return turnManager;
	}

	class PlayerBetBean {
		int betAmount = 0;
		int lastAction = ACTION_PENDING;

		public PlayerBetBean(int totalBet, int lastAction) {
			this.betAmount = totalBet;
			this.lastAction = lastAction;
		}

		public int getBetAmount() {
			return betAmount;
		}

		public int getLastAction() {
			return lastAction;
		}
	}
}
