package pokerserver.players;

import pokerserver.cards.Card;
import pokerserver.cards.PlayerCards;

/**
 * Player related informations like name, id, cards on hands etc.
 * @author Chirag
 *
 */
public class Player {
	private String playerName;
	private int playerId;
	private int totalBalance =1000;
	private boolean isActivePlayer = true;
	private PlayerCards cards;
	private boolean isSmalBlind;
	private boolean isBigBlind;
	private boolean isAllInPlayer;
	private boolean isFoldedPlayer;
//    private int betAmount;
    
	public Player() {
	}
	
	public Player(int pId, String pName) {
		this.playerId = pId;
		this.playerName = pName;
	}

	public void deductBetAmount(int betAmount){
		this.totalBalance -= betAmount;
	}
	public void setSmallBlind(boolean b) {
		this.isSmalBlind = b;
	}

	public boolean isSmallBlind() {
		return this.isSmalBlind;
	}

	public void setBigBlind(boolean b) {
		this.isBigBlind = b;
	}

	public boolean isBigBlind() {
		return this.isBigBlind;
	}

	public void setPlayerId(int pId) {
		this.playerId = pId;
	}

	public void setPlayerName(String pName) {
		this.playerName = pName;
	}

	public String getPlayeName() {
		return playerName;
	}

	public void setTotalBalance(int playersBal) {
		this.totalBalance = playersBal;
	}

//	public void setPlayerBetAmount(int betAmt){
//		this.betAmount=betAmt;
//	}
//	
//	public int  getPlayerBetAmount(){
//		return this.betAmount;
//	}
	
	public int getTotalBalance() {
		return this.totalBalance;
	}

	public void setPlayerActive(boolean isActive) {
		this.isActivePlayer = isActive;
	}

	public void setCards(Card card1, Card card2) {
		this.cards = new PlayerCards(card1, card2);
		System.out.println("Default card 1: "+getPlayeName()+" = " + card1.getCardName());
		System.out.println("Default card 2: "+getPlayeName()+" = " + card2.getCardName());
	}

	public PlayerCards  getPlayerCards(){
		return this.cards;
	}
	
	public Card getFirstCard() {
		return this.cards.getFirstCard();
	}

	public Card getSecondCard() {
		return this.cards.getSecondCard();
	}

	public int getPlayerId() {
		return this.playerId;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public boolean getPlayerActive() {
		return this.isActivePlayer;
	}

	public boolean isPlayrAllIn() {
		return this.isAllInPlayer;
	}

	public void setPlayerAllIn(boolean b) {
		this.isAllInPlayer = b;
	}

	public boolean isPlayrFolded() {
		return this.isFoldedPlayer;
	}

	public void setPlayerFolded(boolean b) {
		this.isFoldedPlayer = b;
	}
}
