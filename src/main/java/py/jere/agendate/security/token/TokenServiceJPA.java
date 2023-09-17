package py.jere.agendate.security.token;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class TokenServiceJPA implements ITokenService {

	@Autowired
	private TokenRepository repo;

	@Override
	public Token registrar(Token registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public Token guardar(Token guardar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Token> guardarTodos(List<Token> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID id) {
		this.repo.deleteById(id);
		return !this.existe(id);
	}

	@Override
	public boolean existe(UUID id) {
		return this.repo.existsById(id);
	}

	@Override
	public List<Token> buscarActivos() {
		return this.repo.findByRevokedIsFalseAndExpiredIsFalse();
	}
	
	@Override
	public List<Token> buscarInactivos() {
		return this.repo.findByRevokedIsTrueOrExpiredIsTrue();
	}

	@Override
	public List<Token> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Token buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Token buscarUltimo() {
		System.err.println("TokenServiceJPA -> buscarUltimo() -> no implementado.");
		return null;
	}

	@Override
	public Token buscarPorToken(String token) {
		return this.repo.findByToken(token).orElseThrow();
	}

	@Override
	public List<Token> buscarPorUser(UUID id) {
		return this.repo.findByUserId(id);
	}

	@Override
	public List<Token> buscarActivosPorUser(UUID id) {
		return this.repo.findByUserIdAndRevokedIsFalseAndExpiredIsFalse(id);
	}

	@Override
	public List<Token> buscarActivosPorUserPorTipo(UUID id, TokenType tipo) {
		return this.repo.findByUserIdAndTypeAndRevokedIsFalseAndExpiredIsFalse(id, tipo);
	}


	@Override
	public void inactivarTodosLosTokensPorUserPorTipo(UUID id, TokenType tipo) {
		this.repo.revokeAndExpireByIdAndType(id, tipo);
	}

	
}
