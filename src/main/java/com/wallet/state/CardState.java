package com.wallet.state;

import com.wallet.entitis.Card;

public interface CardState {
	void next(Card nextCard);
	void pre(Card preCard);
	
}
