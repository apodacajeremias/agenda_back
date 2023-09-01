package py.jere.agendate.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.jere.agendate.model.entities.Persona;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
 
  private String accessToken;
  private String refreshToken;
  private Persona persona;
//  private Empresa empresa;
}
