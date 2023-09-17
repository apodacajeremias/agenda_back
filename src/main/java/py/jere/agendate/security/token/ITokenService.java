package py.jere.agendate.security.token;

import java.util.List;
import java.util.UUID;

import py.jere.agendate.model.services.ServiceCustom;

public interface ITokenService extends ServiceCustom<Token>{
	Token buscarPorToken(String token);
	List<Token> buscarPorUser(UUID id);
	List<Token> buscarActivosPorUser(UUID id);
	List<Token> buscarActivosPorUserPorTipo(UUID id, TokenType tipo);
	void inactivarTodosLosTokensPorUserPorTipo(UUID id, TokenType tipo);
}
