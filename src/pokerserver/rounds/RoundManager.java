package pokerserver.rounds;

import java.util.ArrayList;

import pokerserver.players.Player;
import pokerserver.turns.TurnManager;
import pokerserver.utils.GameConstants;

public class RoundManager implements GameConstants{
	/**Current status of round. e.g active, finish, pending*/
	int status;
	/** Track all player action in round */
	ArrayList<TurnManager> listTurn = new ArrayList<TurnManager>();
	int currentRound;
	
	public RoundManager(int currentRound) {
		// TODO Auto-generated constructor stub
		this.currentRound=currentRound;
	}
	/**
	 * Add turn record when player performed his action
	 */
	public void addTurnRecord(TurnManager turnManager){
		this.listTurn.add(turnManager);
	}
	/**
	 * Fetch all turn's data in current round
	 * @return ArrayList<TurnManager>
	 */
	public ArrayList<TurnManager> getAllTurnRecords(){
		return listTurn;
	}
	/**
	 * Get current status of round it may be 
	ROUND_STATUS_ACTIVE, ROUND_STATUS_PENDING, ROUND_STATUS_FINISH
	 * @return index
	 */
	public int getStatus() {
		return status;
	}

	public int getRound(){
		return this.currentRound;
	}
	
	/**
	 * Update current status of round it may be 
	 * ROUND_STATUS_ACTIVE, ROUND_STATUS_PENDING, ROUND_STATUS_FINISH
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public int getTotalPlayerBetAmount(Player player){
		int totalBet = 0;
//		boolean playerTurnCompleted = false;
		for(TurnManager turnManager:listTurn){
			if(turnManager.getPlayer().getPlayeName().equals(player.getPlayeName())){
//				playerTurnCompleted=true;				
				totalBet+=turnManager.getBetAmount();
			}
		}
//		if(!playerTurnCompleted)
//			totalBet = 10;
		return totalBet;
	}
	public int getTotalRoundBetAmount(){
		int totalBet = 0;
		for(TurnManager turnManager:listTurn){
			totalBet+=turnManager.getBetAmount();
		}
		return totalBet;
	}
}
