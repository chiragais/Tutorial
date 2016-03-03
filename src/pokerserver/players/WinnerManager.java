package pokerserver.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WinnerManager {

	PlayersManager playerManager;
	ArrayList<Winner> listWinners;
	ArrayList<AllInPlayer> listAllinPotAmounts;
	int totalTableAmount = 0;
	int remainingAmount = 0;

	public WinnerManager(PlayersManager playerMgr) {
		this.playerManager = playerMgr;
		listWinners = new ArrayList<Winner>();
		listAllinPotAmounts = new ArrayList<AllInPlayer>();
	}

	public void addWinner(Winner winner) {
		this.listWinners.add(winner);
	}

	public void addAllInTotalPotAmount(AllInPlayer allInPlayer) {
		this.listAllinPotAmounts.add(allInPlayer);
	}

	public int getPlayerWinningAmount(String playerName) {
		int winningAmount = 0;
		for (Winner winner : listWinners) {
			if (winner.getPlayer().getPlayeName().equals(playerName)) {
				winningAmount = winner.getWinningAmount();

			}
		}
		return winningAmount;
	}

	public int getAllInPotAmount(String playerName) {
		int allInPotAmount = 0;
		for (AllInPlayer allInplayer : listAllinPotAmounts) {
			if (allInplayer.getPlayeName().equals(playerName)) {
				allInPotAmount = allInplayer.getTotalAllInPotAmount();
			}
		}
		return allInPotAmount;
	}

	public void setTotalTableAmount(int amount) {
		this.totalTableAmount = amount;
	}

	public void setPlayerWinningAmount(String playerName, int winningAmount) {

		for (Winner winner : listWinners) {
			if (winner.getPlayer().getPlayeName().equals(playerName)) {
				winner.setWinningAmount(winningAmount);
				break;
			}
		}

	}

	public List<PlayerBean> generateWinnerPlayers() {
		List<PlayerBean> listWinnerPlayer = new ArrayList<PlayerBean>();
		// Sort winner players
		Collections.sort(playerManager.getAllAvailablePlayers(),
				new Comparator<PlayerBean>() {
					@Override
					public int compare(PlayerBean player1, PlayerBean player2) {
						return player1.getHandRank().compareTo(
								player2.getHandRank());
					}
				});
		for (int i = 0; i < playerManager.getAllAvailablePlayers().size(); i++) {
			List<PlayerBean> sameRankPlayer = new ArrayList<PlayerBean>();
			PlayerBean currentPlayer = playerManager.getAllAvailablePlayers()
					.get(i);
			if (!listWinnerPlayer.contains(currentPlayer)) {
				sameRankPlayer.add(currentPlayer);
				for (int j = i + 1; j < playerManager.getAllAvailablePlayers()
						.size(); j++) {
					PlayerBean nextPlayer = playerManager
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

		for (PlayerBean playerBean : listWinnerPlayer) {
			System.out.println("Current Pla : " + playerBean.getPlayeName()
					+ " >> " + playerBean.getBestHandRankTotal() + " >> "
					+ playerBean.getHandRank());
		}
		return listWinnerPlayer;
	}

	public void findWinnerPlayers() {
		System.out.println("\n Find Winner Player ------------");
		for (PlayerBean player : generateWinnerPlayers()) {
			if (player.isPlayerActive()) {
				if (!player.isPlayrAllIn()) {
					Winner winner = new Winner(player, totalTableAmount);
					winner.getPlayer().setTotalBalance(
							winner.getPlayer().getTotalBalance()
									+ winner.getWinningAmount());
					totalTableAmount = 0;
					listWinners.add(winner);
					break;
				} else {
					if (getAllInPotAmount(player.getPlayeName()) < totalTableAmount) {
						Winner winner = new Winner(player,
								getAllInPotAmount(player.getPlayeName()));
						winner.getPlayer().setTotalBalance(
								winner.getPlayer().getTotalBalance()
										+ winner.getWinningAmount());
						totalTableAmount -= getAllInPotAmount(player
								.getPlayeName());
						listWinners.add(winner);
					} else {
						Winner winner = new Winner(player, totalTableAmount);
						winner.getPlayer().setTotalBalance(
								winner.getPlayer().getTotalBalance()
										+ winner.getWinningAmount());
						totalTableAmount = 0;
						listWinners.add(winner);
						break;
					}
				}
			}
		}
		if (totalTableAmount != 0) {
			remainingAmount = totalTableAmount;
		}
		System.out.println("\n ---------------------------------");
		for (Winner player : listWinners) {
			System.out.println("\n Winner Player =  "
					+ player.getPlayer().getPlayeName() +" :: Amount : "+player.getWinningAmount());
		}

	}

	public List<AllInPlayer> getAllInPlayers(){
		return listAllinPotAmounts;
	}
	public ArrayList<Winner> getWinnerList() {
		return listWinners;
	}

	public Winner getTopWinner() {
		return listWinners.get(0);
	}

}
