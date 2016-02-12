package pokerserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pokerserver.cards.Card;
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
public class WAGameManager implements GameConstants {

	PlayersManager playersManager;
	HandManager handManager;
	public GamePlay gamePlay;
	ArrayList<Card> listDefaultCards = new ArrayList<Card>();
	RoundManager startRound;
	RoundManager firstFlopRound;
	RoundManager secondFlopRound;
	RoundManager whoopAssRound;
	RoundManager thirdRound;
	int currentRound = 0;

	public WAGameManager() {
		// TODO Auto-generated constructor stub
		playersManager = new PlayersManager();
		initGameRounds();
	}

	public void initGameRounds() {
		System.out.println();
		System.out
				.print("================== WA Game started ==================");

		generateDefaultCards();
		startRound = new RoundManager(WA_ROUND_START);
		firstFlopRound = new RoundManager(WA_ROUND_FIRST_FLOP);
		secondFlopRound = new RoundManager(WA_ROUND_SECOND_FLOP);
		whoopAssRound = new RoundManager(WA_ROUND_WHOOPASS);
		thirdRound = new RoundManager(WA_ROUND_THIRD_FLOP);
		handManager = new HandManager(listDefaultCards);
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

	/**
	 * Start first round and set other round status as a pending
	 */
	public void startFirstRound() {
		currentRound = WA_ROUND_START;
		System.out.println();
		System.out.print(">>>>>>>>>>> WA start Round started");
		startRound.setStatus(ROUND_STATUS_ACTIVE);
		firstFlopRound.setStatus(ROUND_STATUS_PENDING);
		secondFlopRound.setStatus(ROUND_STATUS_PENDING);
		whoopAssRound.setStatus(ROUND_STATUS_PENDING);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startFirstFlopRound() {
		currentRound = WA_ROUND_FIRST_FLOP;
		System.out.println();
		System.out.print(">>>>>>>>>>>WA First Flop Round started  ");
		startRound.setStatus(ROUND_STATUS_FINISH);
		firstFlopRound.setStatus(ROUND_STATUS_ACTIVE);
		secondFlopRound.setStatus(ROUND_STATUS_PENDING);
		whoopAssRound.setStatus(ROUND_STATUS_PENDING);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startSecondFlopRound() {
		currentRound = WA_ROUND_SECOND_FLOP;
		System.out.println();
		System.out.print(">>>>>>>>>>> WA Second flop Round started  ");
		startRound.setStatus(ROUND_STATUS_FINISH);
		firstFlopRound.setStatus(ROUND_STATUS_FINISH);
		secondFlopRound.setStatus(ROUND_STATUS_ACTIVE);
		whoopAssRound.setStatus(ROUND_STATUS_PENDING);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startWhoopAssRound() {
		currentRound = WA_ROUND_WHOOPASS;
		System.out.println();
		System.out.print(">>>>>>>>>>> WA WhoopAss Round started  ");
		startRound.setStatus(ROUND_STATUS_FINISH);
		firstFlopRound.setStatus(ROUND_STATUS_FINISH);
		secondFlopRound.setStatus(ROUND_STATUS_FINISH);
		whoopAssRound.setStatus(ROUND_STATUS_ACTIVE);
		thirdRound.setStatus(ROUND_STATUS_PENDING);
	}

	public void startThirdFlopRound() {
		currentRound = WA_ROUND_THIRD_FLOP;
		System.out.println();
		System.out.print(">>>>>>>>>>> WA Third Round started  ");
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

		if (currentRound.getRound() == WA_ROUND_WHOOPASS) {
			if(currentRound.getAllTurnRecords().size()==playersManager.getAllAvailablePlayers().size()){
				return true;
			}
			return false;
		} else {

			for (PlayerBean player : playersManager.getAllAvailablePlayers()) {
				if (player.isPlayerActive()) {
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
			startFirstFlopRound();
			break;
		case WA_ROUND_FIRST_FLOP:
			startSecondFlopRound();
			break;
		case WA_ROUND_SECOND_FLOP:
			startWhoopAssRound();
			break;
		case WA_ROUND_WHOOPASS:
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
		while (listDefaultCards.size() != 6) {
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
		totalBetAmount += startRound.getTotalRoundBetAmount();
		totalBetAmount += firstFlopRound.getTotalRoundBetAmount();
		totalBetAmount += secondFlopRound.getTotalRoundBetAmount();
		totalBetAmount += thirdRound.getTotalRoundBetAmount();
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
			System.out.println();
			System.out.print("Turn Manager # User: "
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
