	package pokerserver.players;

	import java.util.ArrayList;
	import java.util.List;

	import pokerserver.cards.Card;
	import pokerserver.cards.PlayerCards;
	import pokerserver.utils.GameConstants;

	public class WhoopAssHandManager implements GameConstants{
		
		public HAND_RANK pokerHandRank;
		List<Card> defaultCards = new ArrayList<Card>();
		private Card[] handBestCards = new Card[5];
		
		public WhoopAssHandManager(List<Card> defaultCards){
			this.defaultCards.addAll(defaultCards);
			System.out.println();
			System.out.print("Default cards 0: "+ defaultCards.size());
		}
		public Card[] getPlayerBestCards(){
			
			return handBestCards;
		}
		
	
		
		public HAND_RANK findPlayerBestHand(PlayerCards playerCards,Card wACard,int wACardStatus){
			
		//	handBestCards = new Card[5];
			List<Card> allCards = new ArrayList<Card>();
			allCards.addAll(defaultCards);
			allCards.add(playerCards.getFirstCard());
			allCards.add(playerCards.getSecondCard());
			if(wACardStatus!=GameConstants.ACTION_WA_NO){
				allCards.add(wACard);
			}
			allCards = sortingCardsList(allCards);
			List<Card> playerHandList=getPlayerHandList(playerCards, wACard, wACardStatus);
			
			Card[] newCards = new Card[allCards.size()];
			for (int i = 0; i < allCards.size(); i++) {
//				System.out.println();
//				System.out.println("this is order  = " + allCards.get(i).getCardName());
				newCards[i]=allCards.get(i);
			}
			
			if (isRoyalFlush(newCards,playerHandList) && isValidHandBestCards(playerHandList) ) {
				pokerHandRank = HAND_RANK.ROYAL_FLUSH;
			} else if (isAStraightFlush(newCards,playerHandList) && isValidHandBestCards(playerHandList)) {
				pokerHandRank = HAND_RANK.STRAIGHT_FLUSH;
			} else if (isFourOfAKind(newCards,playerHandList)) {
				pokerHandRank = HAND_RANK.FOUR_OF_A_KIND;
			} else if (isFullHouse(newCards,playerHandList) && isValidHandBestCards(playerHandList)) {
				pokerHandRank = HAND_RANK.FULL_HOUSE;
			} else if (isFlush(newCards,playerHandList) && isValidHandBestCards(playerHandList)) {
				pokerHandRank = HAND_RANK.FLUSH;
			} else if (isStraight(newCards,playerHandList)) {
				pokerHandRank = HAND_RANK.STRAIGHT;
			} else if (isAceStraight(newCards,playerHandList)) {
				pokerHandRank = HAND_RANK.STRAIGHT;
			} else if (isThreeOfAKind(newCards,playerHandList)) {
				pokerHandRank = HAND_RANK.THREE_OF_A_KIND;
			} else if (isTwoPair(newCards,playerHandList)) {
				pokerHandRank = HAND_RANK.TWO_PAIR;
			} else if (isPair(newCards,playerHandList)) {
				pokerHandRank = HAND_RANK.PAIR;
			} else {
				 List<Card> highCardList=getHighCards(playerCards, wACard, wACardStatus);
				for (int i = 0; i < highCardList.size(); i++) {
					handBestCards[i] = highCardList.get(i);
					System.out.println("High cards  "+i+" = "+handBestCards[i].getCardName() );
				}
				pokerHandRank = HAND_RANK.HIGH_CARD;
			}
			return pokerHandRank;
		}
		
		
        public List<Card> getHighCards(PlayerCards playerCards,Card wACard,int wACardStatus){
			
			List<Card> playerHandList= new ArrayList<Card>();
			List<Card> playerDefaultList= new ArrayList<Card>();
			if(wACardStatus==GameConstants.ACTION_WA_UP){
				playerHandList.add(playerCards.getFirstCard());
				playerHandList.add(playerCards.getSecondCard());
				playerDefaultList.addAll(defaultCards);
				playerDefaultList.add(wACard);
			}else if(wACardStatus==GameConstants.ACTION_WA_DOWN){
				playerHandList.add(playerCards.getFirstCard());
				playerHandList.add(playerCards.getSecondCard());
				playerHandList.add(wACard);
				playerDefaultList.addAll(defaultCards);
			}else if(wACardStatus==GameConstants.ACTION_WA_NO){
				playerHandList.add(playerCards.getFirstCard());
				playerHandList.add(playerCards.getSecondCard());
				playerDefaultList.addAll(defaultCards);
			}
			playerHandList=sortingCardsList(playerHandList);
			playerDefaultList=sortingCardsList(playerDefaultList);
			
			List<Card> playerHighCardList= new ArrayList<Card>();
			playerHighCardList.add(playerHandList.get(0));
			playerHighCardList.add(playerHandList.get(1));
			playerHighCardList.add(playerDefaultList.get(0));
			playerHighCardList.add(playerDefaultList.get(1));
			playerHighCardList.add(playerDefaultList.get(2));
			playerHighCardList=sortingCardsList(playerHighCardList);
	
			return playerHighCardList;
			
		}
		
        public List<Card> getPlayerHandList(PlayerCards playerCards,Card wACard,int wACardStatus){
        	  List<Card> playerHandList= new ArrayList<Card>();
  			if(wACardStatus==GameConstants.ACTION_WA_UP || wACardStatus==GameConstants.ACTION_WA_NO){
  				playerHandList.add(playerCards.getFirstCard());
  				playerHandList.add(playerCards.getSecondCard());
  			    
  			}else if(wACardStatus==GameConstants.ACTION_WA_DOWN){
  				playerHandList.add(playerCards.getFirstCard());
  				playerHandList.add(playerCards.getSecondCard());
  				playerHandList.add(wACard);
  			}
  			 
  			playerHandList=sortingCardsList(playerHandList);
  			
  			return playerHandList;
        }
        
        
		public boolean isValidHandBestCards(List<Card> playerHandList){
							
			if(checkCardsOccurance(playerHandList)){
		    	return true;
		    }else{
		    	return false;
		    }
		}
		
		public boolean checkCardsOccurance(List<Card> playerHandList){
			int noOfMatchCount=0;
			for(Card cH : playerHandList){
					for(Card cP : handBestCards){
						if(cH.getCardName().equals(cP.getCardName())){
							noOfMatchCount++;
						}
					  }
					}
				   if(noOfMatchCount==2){
					   return true;
				   }else{
					   return false;
				   }
			
		}
		
		
		
		
//		public HAND_RANK findPlayerBestHand(PlayerCards playerCards){
//		
//			handBestCards = new Card[5];
//			List<Card> allCards = new ArrayList<Card>();
//			allCards.addAll(defaultCards);
//			allCards.add(playerCards.getFirstCard());
//			allCards.add(playerCards.getSecondCard());
//			allCards = shortingCards(allCards);
//			
//			Card[] newCards = new Card[allCards.size()];
//			for (int i = 0; i < allCards.size(); i++) {
////				System.out.println();
////				System.out.println("this is order  = " + allCards.get(i).getCardName());
//				newCards[i]=allCards.get(i);
//			}
//			
//			if (isRoyalFlush(newCards)) {
//				pokerHandRank = HAND_RANK.ROYAL_FLUSH;
//			} else if (isAStraightFlush(newCards)) {
//				pokerHandRank = HAND_RANK.STRAIGHT_FLUSH;
//			} else if (isFourOfAKind(newCards)) {
//				pokerHandRank = HAND_RANK.FOUR_OF_A_KIND;
//			} else if (isFullHouse(newCards)) {
//				pokerHandRank = HAND_RANK.FULL_HOUSE;
//			} else if (isFlush(newCards)) {
//				pokerHandRank = HAND_RANK.FLUSH;
//			} else if (isStraight(newCards)) {
//				pokerHandRank = HAND_RANK.STRAIGHT;
//			} else if (isAceStraight(newCards)) {
//				pokerHandRank = HAND_RANK.STRAIGHT;
//			} else if (isThreeOfAKind(newCards)) {
//				pokerHandRank = HAND_RANK.THREE_OF_A_KIND;
//			} else if (isTwoPair(newCards)) {
//				pokerHandRank = HAND_RANK.TWO_PAIR;
//			} else if (isPair(newCards)) {
//				pokerHandRank = HAND_RANK.PAIR;
//			} else {
//				for (int i = 0; i < handBestCards.length; i++) {
//					handBestCards[i] = allCards.get(i);
//				}
//				pokerHandRank = HAND_RANK.HIGH_CARD;
//			}
//			return pokerHandRank;
//		}
		
		public List<Card> sortingCardsList(List<Card> allCards){
			for (int i = 1; i <= allCards.size(); i++) {
				for (int j = 0; j < allCards.size()- i; j++) {
					if (allCards.get(j + 1).getValue() > allCards.get(j).getValue()) {
						Card cTemp = allCards.get(j);
						allCards.set(j, allCards.get(j + 1));
						allCards.set(j + 1, cTemp);
					}
				}
			}
			
			return allCards;
		}

		public Card[] sortingCardsArray(Card[] cardArray) {
			for (int i = 1; i <= cardArray.length; i++) {
				for (int j = 0; j < cardArray.length - i; j++) {
					if (cardArray[j + 1].getValue() > cardArray[j].getValue()) {
						Card cTemp = cardArray[j];
						cardArray[j] = cardArray[j + 1];
						cardArray[j + 1] = cTemp;
					}
				}
			}
			return cardArray;
		}
		public boolean isRoyalFlush(Card[] allCards,List<Card> playerHandList) {

			if (isFlush(allCards,playerHandList)) {
				if (isStraight(handBestCards,playerHandList)) {
					boolean aceExists = false, kingExists = false, queenExists = false, jackExists = false, tenExists = false;
					for (Card card : handBestCards) {
						switch (card.getRank().toString()) {
						case RANK_ACE:
							aceExists = true;
							break;
						case RANK_KING:
							kingExists = true;
							break;
						case RANK_QUEEN:
							queenExists = true;
							break;
						case RANK_JACK:
							jackExists = true;
							break;
						case RANK_TEN:
							tenExists = true;
							break;
						}
					}
					
					if(isValidHandBestCards(playerHandList))
					{
						return (aceExists && kingExists && queenExists && jackExists && tenExists);
						
					}else{
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		public boolean isStraight(Card[] allCards,List<Card> playerHandList) {
			int lastPos = 0;
			int noOfCardsInARow = 0;
			int pos = 0;
			boolean isAStraight = false;
			while (pos < allCards.length - 1 && !isAStraight) {
				if (allCards[pos].getValue() - allCards[pos + 1].getValue() == 1) {
					noOfCardsInARow++;
					if (noOfCardsInARow == 4) {
						isAStraight = true;
						lastPos = pos + 1;

					} else {
						pos++;
					}
				} else if (allCards[pos].getValue() - allCards[pos + 1].getValue() == 0) {

					pos++;
				} else {
					noOfCardsInARow = 0;
					pos++;
				}
			}
			if (isAStraight) {
				System.out.println("this is pokerhand    "+"  "+allCards.length);
				System.out.println("this is pokerhand    "+"  "+handBestCards.length);
				
				for (int i=0,j=0; i < allCards.length - 1; i++) {

					if ((allCards[i].getValue() - allCards[i + 1].getValue()) == 1) {
						handBestCards[j] = allCards[i];
						
					    System.out.println("this is pokerhand    "+j+"  "+handBestCards[j]);
						j++;
						if(j==handBestCards.length-1){
							handBestCards[j]=allCards[i+1];
							 System.out.println("this is pokerhand    "+j+"  "+handBestCards[j]);
							break;
						}
					}

				}
				if(isValidHandBestCards(playerHandList))
				{
					return isAStraight;
					
				}else{
					return false;
				}
			}

			return isAStraight;
		}

		public boolean isAceStraight(Card[] allCards,List<Card> playerHandList) {
			if (allCards[0].getRank() == RANKS.ace) {
				boolean aceExist = false, twoExist = false, threeExist = false, fourExist = false, fiveExist = false;
				for (Card card : allCards) {

					switch (card.getRank().toString()) {
					case RANK_ACE:
						aceExist = true;
						break;
					case RANK_TWO:
						twoExist = true;
						break;
					case RANK_THREE:
						threeExist = true;
						break;
					case RANK_FOUR:
						fourExist = true;
						break;
					case RANK_FIVE:
						fiveExist = true;
						break;

					}
				}
				if (aceExist && twoExist && threeExist && fourExist && fiveExist) {
					for (int i = 1; i < handBestCards.length; i++) {
						handBestCards[i - 1] = allCards[i];
					}
					handBestCards[4] = allCards[0];
				}
				if(isValidHandBestCards(playerHandList))
				{
					return (aceExist && twoExist && threeExist && fourExist && fiveExist);
					
				}else{
					return false;
				}
				
				
			} else {
				return false;
			}
		}

		public boolean isFlush(Card[] allCards,List<Card> playerHandList) {

			int noOfClubs = 0;
			int noOfSpades = 0;
			int noOfHearts = 0;
			int noOfDiamonds = 0;
			for (Card c : allCards) {
				switch (c.getSuit().toString()) {
				case SUIT_HEART:
					noOfHearts++;
					break;
				case SUIT_SPADE:
					noOfSpades++;
					break;
				case SUIT_CLUB:
					noOfClubs++;
					break;
				case SUIT_DIAMOND:
					noOfDiamonds++;
					break;
				}
			}

			if (noOfClubs >= 5) {
				int j = 0;
				for (int i = 0; i < allCards.length; i++) {
					if (allCards[i].getSuit() == SUITS.club) {
						handBestCards[j] = allCards[i];
						j++;
						if (j == handBestCards.length) {
							return true;
						}
					}
				}
			} else if (noOfSpades >= 5) {
				int j = 0;
				for (int i = 0; i < allCards.length; i++) {
					if (allCards[i].getSuit() == SUITS.spade) {
						handBestCards[j] = allCards[i];
						j++;
						if (j == handBestCards.length) {
							return true;
						}
					}
				}
			} else if (noOfHearts >= 5) {
				int j = 0;
				for (int i = 0; i < allCards.length; i++) {
					if (allCards[i].getSuit() == SUITS.heart) {
						handBestCards[j] = allCards[i];
						j++;
						if (j == handBestCards.length) {
							return true;
						}
					}
				}
			} else if (noOfDiamonds >= 5) {
				int j = 0;
				for (int i = 0; i < allCards.length; i++) {
					if (allCards[i].getSuit() == SUITS.diamond) {
						handBestCards[j] = allCards[i];
						j++;
						if (j == handBestCards.length) {
							return true;
						}
					}
				}
			}
			return false;
		}

//		private boolean isThreeOfAKind(Card[] allCards) {
//			int lastSameRankOffset=0;
//			int cardRepeats = 1;
//			boolean isThreeOfAKind = false;
//			int m = 0;
//			int n = m + 1;
//			while (m < allCards.length && !isThreeOfAKind) {
//				cardRepeats = 1;
//				n = m + 1;
//				while (n < allCards.length && !isThreeOfAKind) {
//					if (allCards[m].getValue() == allCards[n].getValue()) {
//						cardRepeats++;
//						if (cardRepeats == 3) {
//							handBestCards[0] = allCards[n];
//							lastSameRankOffset=n;
//							isThreeOfAKind = true;
//						}
//					}
//					n++;
//				}
//				m++;
//			}
//			if (isThreeOfAKind) {
//				calculateFinalKindOfArray(2, allCards,lastSameRankOffset);
//			}
//			return isThreeOfAKind;
//		}

		
	private boolean isThreeOfAKind(Card[] allCards,List<Card> playerHandList) {
			int lastSameRankOffset=0;
			int cardRepeats = 1;
			boolean isThreeOfAKind = false;
			int ThreeOFaKindValue=0;
			int m = 0;
			int n = m + 1;
			while (m < allCards.length && !isThreeOfAKind) {
				cardRepeats = 1;
				n = m + 1;
				while (n < allCards.length && !isThreeOfAKind) {
					if (allCards[m].getValue() == allCards[n].getValue()) {
						cardRepeats++;
						if (cardRepeats == 3) {
							handBestCards[0] = allCards[n];
							ThreeOFaKindValue=allCards[n].getValue();
							lastSameRankOffset=n;
							isThreeOfAKind = true;
						}
					}
					n++;
				}
				m++;
			}
			if (isThreeOfAKind) {
				calculateFinalKindOfArray(2, allCards,lastSameRankOffset);
				if(isValidHandBestCards(playerHandList))
				{
					return isThreeOfAKind;
				}else{
					checkExplicityCardOccurance(playerHandList,ThreeOFaKindValue);
					return isThreeOfAKind;
				}
			}
				
			return isThreeOfAKind;
		}
		
		
		public void checkExplicityCardOccurance(List<Card> playerHandList,int threeOfKindValue){
			int countExist=2;
			   for(Card cH : playerHandList){
					for(Card cP : handBestCards){
						if(cH.getCardName().equals(cP.getCardName())){
							countExist--;
							playerHandList.remove(cH);
						}
					  }
					}
			   checkExplicitly(countExist, playerHandList,threeOfKindValue);
			
		}
		
		
		
		public void calculateFinalKindOfArray(int nPlace, Card[] allCards,int offset) {
			Card cTemp = handBestCards[0];
			for (int i = 0; i < handBestCards.length; i++) {
				if (i < nPlace) {
					if (cTemp.getValue() < allCards[i].getValue()) {
						handBestCards[i] = allCards[i];
					} else {
						for (int j = 0; j < handBestCards.length; j++) {
							handBestCards[j] = allCards[j];
						}
						break;
					}
				} else {
					handBestCards[i] = allCards[offset--];
				}
			}
		}
		
		
		public void  checkExplicitly(int nPlace, List<Card> playerHandCard,int repeatedCardValue) {
//			Card cTemp = handBestCards[0];
			int count=0;
			for (int i = 0; i < handBestCards.length; i++) {
				if (count < nPlace) {
					if (repeatedCardValue!=handBestCards[i].getValue()) {
						handBestCards[i] = playerHandCard.get(count);
						count++;
					} 
			   }else{
				   break;
			   }
			}
			
			handBestCards=sortingCardsArray(handBestCards);
		}
		
		private boolean isTwoPair(Card[] allCards,List<Card> playerHandList) {
	        
			int lastSameOffsetOne=0;
			int lastSameOffsetSecond=0;
			
			Card cTemp1 = null, cTemp2 = null;
			int cardRepeats = 1;
			int noOfCardRepeats = 0;
			boolean isTwoPair = false;
			int m = 0;
			int n = m + 1;
			while (m < allCards.length && !isTwoPair) {
				cardRepeats = 1;
				n = m + 1;
				while (n < allCards.length && !isTwoPair) {
					if (allCards[m].getValue() == allCards[n].getValue()) {
						cardRepeats++;
						if (cardRepeats == 2) {
							cardRepeats = 1;
							noOfCardRepeats++;
							if (noOfCardRepeats == 1) {
								cTemp1 = allCards[n];
								lastSameOffsetOne=n;
							}
							if (noOfCardRepeats == 2) {
								cTemp2 = allCards[n];
								lastSameOffsetSecond=n;
								isTwoPair = true;

							}
						}

					}
					n++;
				}
				m++;
			}
			if (isTwoPair) {
				if (allCards[0].getValue() > cTemp1.getValue()) {
					handBestCards[0] = allCards[0];
					handBestCards[1] =  allCards[lastSameOffsetOne--];
					handBestCards[2] =  allCards[lastSameOffsetOne--];
					handBestCards[3] =  allCards[lastSameOffsetSecond--];
					handBestCards[4] =  allCards[lastSameOffsetSecond--];
				} else {
					handBestCards[0] =  allCards[lastSameOffsetOne--];
					handBestCards[1] =  allCards[lastSameOffsetOne--];
					if (allCards[2].getValue() > cTemp2.getValue()) {
						handBestCards[2] = allCards[2];
						handBestCards[3] =  allCards[lastSameOffsetSecond--];
						handBestCards[4] =  allCards[lastSameOffsetSecond--];
					} else {
						handBestCards[2] =  allCards[lastSameOffsetSecond--];
						handBestCards[3] =  allCards[lastSameOffsetSecond--];
						handBestCards[4] = allCards[4];
					}
				}
				
				if(isValidHandBestCards(playerHandList))
				{
					return isTwoPair;
				}else if(checkPairInPlayerList(playerHandList)){
					return isTwoPair;
				}else if(checkForSinglePlayerCards(allCards,playerHandList)){
					return isTwoPair;
				}else{
					return false;
				}
		 	}

			return isTwoPair;
		}

		
		public boolean checkForSinglePlayerCards(Card[] allCards,List<Card> playerHandList){
		List<Card> 	playerDefaultList=sortingCardsList(defaultCards);
		int k=0;
		    Card[] bestCardList = new Card[5];
				for(int i=0;i<playerHandList.size();i++){
					for(int j=0;j<playerDefaultList.size();j++){
					if(playerHandList.get(i).getValue()==playerDefaultList.get(j).getValue()){
						bestCardList[k]=playerHandList.get(i);
						bestCardList[++k]=playerDefaultList.get(j);
						if(k==bestCardList.length-1){
							bestCardList[k]=handBestCards[0];
							for(int m=0;m<bestCardList.length;m++){
								handBestCards[m]=bestCardList[m];
							}
							handBestCards=sortingCardsArray(handBestCards);
						 return true;
						}
						continue;
					}
						
				}
				
			}
				return false;
			
		}
		
		
		public boolean checkPairInPlayerList(List<Card> playerCardList){
			for(int i=0;i<playerCardList.size()-1;i++){
				if(playerCardList.get(i).getValue()==playerCardList.get(i+1).getValue()){
					handBestCards[handBestCards.length-1]=playerCardList.get(i+1);
					handBestCards[handBestCards.length-2]=playerCardList.get(i);
					return true;
				}
			}
			return false;
			
		}
		
		
		private boolean isPair(Card[] allCards,List<Card> playerHandList) {
			int lastSameRankOffset=0;
			int cardRepeats = 1;
			boolean isPair = false;
			int m = 0;
			int n = m + 1;
			while (m < allCards.length && !isPair) {
				cardRepeats = 1;
				n = m + 1;
				while (n < allCards.length && !isPair) {
					if (allCards[m].getValue() == allCards[n].getValue()) {
						cardRepeats++;
						if (cardRepeats == 2) {
							isPair = true;
							handBestCards[0] = allCards[n];
							lastSameRankOffset=n;
							break;
						}
					}
					n++;
				}

				m++;
			}
			if (isPair) {
				calculateFinalKindOfArray(3, allCards,lastSameRankOffset);
				if(isValidHandBestCards(playerHandList))
				{
					return isPair;
				}else{
				if(checkPairInPlayerList(playerHandList)){
					return isPair;
				}else{
					return false;
				}
			   }
				
			}
			return isPair;
		}

		private boolean isFullHouse(Card[] allCards,List<Card> playerHandList) {
	        
			int lastSameRankOffset=0;
			Card cTemp = null;
			int ThreeOfKindCardValue = 0;
			int noOfRepeats = 1;
			int j = 0;
			boolean isThreeOfAKind = false;
			boolean isTwoOfAKind = false;
			for (int i = 0; i < allCards.length; i++) {
				j = i + 1;
				noOfRepeats = 1;
				while (j < allCards.length) {
					if (allCards[i].getValue() == allCards[j].getValue()) {
						noOfRepeats++;
						if (noOfRepeats == 3) {
							ThreeOfKindCardValue = allCards[i].getValue();
							isThreeOfAKind = true;
							cTemp = allCards[j];
							lastSameRankOffset=j;
							noOfRepeats = 1;
							break;
						}
					}
					j++;
				}
				if (isThreeOfAKind) {
					for (int k = 0; k < 3; k++) {
						handBestCards[k] = allCards[lastSameRankOffset--];
						
					}
					break;
				}
			}
			if (isThreeOfAKind) {
				
				for (int i = 0; i < allCards.length - 1; i++) {
					j = i + 1;
					noOfRepeats = 1;
					if (allCards[i].getValue() != ThreeOfKindCardValue) {
						while (j < allCards.length) {
							if (allCards[i].getValue() == allCards[j].getValue()) {
								noOfRepeats++;
								if (noOfRepeats == 2) {
									isTwoOfAKind = true;
									cTemp = allCards[j];
									lastSameRankOffset=j;
									noOfRepeats = 1;
									break;
								}
							}
							j++;
						}
					}
					if (isTwoOfAKind) {
						for (int k = 3; k < handBestCards.length; k++) {
							
							handBestCards[k] =  allCards[lastSameRankOffset--];
						}
						handBestCards = sortingCardsArray(handBestCards);
						break;
					}
				}
			}
			return (isTwoOfAKind && isThreeOfAKind);

		}

		public boolean isFourOfAKind(Card[] allCards,List<Card> playerHandList) {
			int lastSameRankOffset=0;
			int cardRepeats = 1;
			boolean isFourOfAKind = false;
			int m = 0;
			int n = m + 1;
			while (m < allCards.length && !isFourOfAKind) {
				cardRepeats = 1;
				n = m + 1;
				while (n < allCards.length && !isFourOfAKind) {
					if (allCards[m].getValue() == allCards[n].getValue()) {
						cardRepeats++;
						if (cardRepeats == 4) {
							handBestCards[0] = allCards[n];
							lastSameRankOffset=n;
							isFourOfAKind = true;
						}
					}
					n++;
				}
				m++;
			}
			if (isFourOfAKind) {
				calculateFinalKindOfArray(1, allCards,lastSameRankOffset);
				
				if(isValidHandBestCards(playerHandList))
				{
					return isFourOfAKind;
					
				}else{
					return false;
				}

			}

			
			
			return isFourOfAKind;
		}

		private boolean isAStraightFlush(Card[] allCards,List<Card> playerHandList) {
			if (isFlush(allCards,playerHandList)) {
				if (isStraight(handBestCards,playerHandList)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

//		public Card[] GetPokerHandArray() {
		public ArrayList<Card> getPokerHandArray() {
			ArrayList< Card> pokerHands=  new ArrayList<Card>();
			for(Card c : handBestCards){
				pokerHands.add(c);
			}
			return pokerHands;
		//	return this.PokerHandFinalCards;
		}
		
		public ArrayList<String> getWinnerHandCardNameList() {
			ArrayList< String> pokerHands=  new ArrayList<String>();
			for(Card c : handBestCards){
				pokerHands.add(c.getCardName());
			}
			return pokerHands;
		//	return this.PokerHandFinalCards;
		}
	}


