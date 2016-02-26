package pokerserver.players;

import java.util.ArrayList;
import java.util.List;

import pokerserver.cards.Card;
import pokerserver.cards.PlayerCards;
import pokerserver.utils.GameConstants.HAND_RANK;

/**
 * Player related informations like name, id, cards on hands etc.
 * 
 * @author Chirag
 * 
 */
public class PlayerBean {
	
	
	private String playerName;
	private int playerId;
	private int totalBalance = 1000;
	private boolean isActivePlayer = true;
	private PlayerCards cards;
	private Card waCard;
	private int wACardStatus;
	
	private boolean isSmalBlind;
	private boolean isBigBlind;
	private boolean isAllInPlayer;
	private boolean isFoldedPlayer;
	private HAND_RANK handRank;
	private Card[] handBestCards = new Card[5];
    
	// private int betAmount;

	public PlayerBean() {
	}

	public PlayerBean(int pId, String pName) {
		this.playerId = pId;
		this.playerName = pName;
	}

	public void deductBetAmount(int betAmount) {
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

	// public void setPlayerBetAmount(int betAmt){
	// this.betAmount=betAmt;
	// }
	//
	// public int getPlayerBetAmount(){
	// return this.betAmount;
	// }
	public void setWACardStatus(int status){
		this.wACardStatus=status;
	}
	
	public int getWACardStatus(){
		return this.wACardStatus;
	}
	public Card getWACard() {
		return waCard;
	}

	public int getTotalBalance() {
		return this.totalBalance;
	}

	public void setPlayerActive(boolean isActive) {
		this.isActivePlayer = isActive;
	}

	public void setCards(Card card1, Card card2, Card waCard) {
		this.cards = new PlayerCards(card1, card2);
		this.waCard = waCard;
		System.out.println("Default card 1: " + getPlayeName() + " = "
				+ card1.getCardName());
		System.out.println("Default card 2: " + getPlayeName() + " = "
				+ card2.getCardName());
		System.out.println("WA Card : " + waCard.getCardName());
	}

	public PlayerCards getPlayerCards() {
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

	public boolean isPlayerActive() {
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

	public void setPlayersBestHand(HAND_RANK hand, Card[] listCard) {
		this.handRank = hand;
		this.handBestCards = listCard;
		// System.out.println();
		// System.out.print("Player Hand : "+ handRank);
		
	}

	public HAND_RANK getHandRank() {
		return handRank;
	}

	// public Card[] getBestHandCards(){
	// return handBestCards;
	// }
	public ArrayList<Card> getBestHandCards() {
		ArrayList<Card> pokerHands = new ArrayList<Card>();
		for (Card c : handBestCards) {
			pokerHands.add(c);
		}
		return pokerHands;

	}

	
	public Card[] getBestHandCardsArray() {
		
		return handBestCards;

	}
	
	
	public List<String> getBestHandCardsName() {
		List<String> listCard = new ArrayList<String>();
		for (Card card : handBestCards) {
			listCard.add(card.getCardName());
		}
		return listCard;
	}
}
