/**
 * 
 */
package services;

import iservice.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import data.Token;
import database.TokenDAO;
import exceptions.AccountNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.TokenNotFoundException;

/**
 * Logic behind tokens
 */
public class TokenService extends Service<TokenDAO> {
	
	/** Creates a TokenService */
	TokenService(TokenDAO dao) {
		super(dao);
	}

	/** Logs the user in */
	public String login(String netid, String password) throws InternalServerException, InvalidCredentialsException {
		return run(dao -> {
			int idAccount;
			idAccount = Services.getAccountService().getId(netid, password);
			
			Date now = Calendar.getInstance().getTime();
			
			Token token = dao.get(idAccount);
			if (token != null) {
				String tokenStr = token.getToken();
				dao.update(tokenStr, now);
				return tokenStr;
			}
			
			String tokenStr = new BigInteger(150, new SecureRandom()).toString(32);
			dao.create(tokenStr, idAccount, now);
			return tokenStr;
		})
		.process(AccountNotFoundException.class, new InvalidCredentialsException())
		.unwrap();
	}
	
	/** Get the token info for the given token */
	public Token getTokenInfo(String tokenStr) throws InternalServerException, TokenNotFoundException {
		return run(dao -> {
			Token token = dao.get(tokenStr);
			if (token == null) throw new TokenNotFoundException();
			return token;
		})
		.process(TokenNotFoundException.class)
		.unwrap();
	}
	
	/** Logs the user out */
	public void logout(String token) throws InternalServerException {
		run(dao -> {
			dao.delete(token);
		})
		.unwrap();
	}
}
