package py.podac.tech.agenda.model.enums;

/**
 * Tipo de descuentos, por ejemplo: Si el beneficio de Transaccion tiene
 * Tipo.VALOR y descuento = 5000 La transaccion hace total - descuento (EN VALOR
 * MONEDA) Si el beneficio es PORCENTAJE y descuento = 10 La transaccion calcula
 * total menos el descuento (QUE ES PORCENTUAL)
 * 
 * @author jeremias
 *
 */
public enum TipoDescuento {
	VALOR, PORCENTAJE
}
