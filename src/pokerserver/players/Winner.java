package pokerserver.players;

import java.util.ArrayList;
import java.util.List;

import pokerserver.cards.Card;

public class Winner {
  private	PlayerBean player;
  private	int winningAmount=0;
  private  List<Card> handCards=new ArrayList<Card>();	 
  public Winner(PlayerBean player,int winningAmount){
		   this.player=player;
	       this.winningAmount=winningAmount;
	       this.handCards=player.getBestHandCards();
	   }

  
  public ArrayList<Card> getBestHandCards() {
		ArrayList<Card> pokerHands = new ArrayList<Card>();
		for (Card c : handCards) {
			pokerHands.add(c);
		}
		return pokerHands;

	}

  
  public List<String> getBestHandCardsName() {
		List<String> listCard = new ArrayList<String>();
		for (Card card : handCards) {
			listCard.add(card.getCardName());
		}
		return listCard;
	}
  
    public PlayerBean getPlayer(){
    	return player;
    }
    
    public void setWinningAmount(int winningAmount) {
		this.winningAmount = winningAmount;
	}
	
	public int getWinningAmount() {
		return this.winningAmount;
	}
  
   
  
}
