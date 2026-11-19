package minimercado.api.producto;

import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InventarioClient {
    private final RestClient cliente;

    public InventarioClient(RestClient.Builder builder,
                            @Value("${todomarket.inventory.url:http://localhost:8183}") String url,
                            @Value("${INVENTORY_TOKEN:}") String token) {
        var fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofSeconds(3));
        fabrica.setReadTimeout(Duration.ofSeconds(7));
        RestClient.Builder configurado = builder.clone().baseUrl(url).requestFactory(fabrica);
        if (!token.isEmpty()) configurado.defaultHeader("X-TodoMarket-Token", token);
        this.cliente = configurado.build();
    }

    public List<ProductoResponse> listar() {
        return listar("/api/productos");
    }

    public ProductoResponse buscar(int id) {
        return cliente.get().uri("/api/productos/{id}", id).retrieve().body(ProductoResponse.class);
    }

    public ProductoResponse crear(ProductoRequest solicitud) {
        return cliente.post().uri("/api/productos").body(solicitud).retrieve().body(ProductoResponse.class);
    }

    public ProductoResponse actualizar(int id, ProductoRequest solicitud) {
        return cliente.put().uri("/api/productos/{id}", id).body(solicitud).retrieve().body(ProductoResponse.class);
    }

    public void eliminar(int id) { cliente.delete().uri("/api/productos/{id}", id).retrieve().toBodilessEntity(); }

    public List<ProductoResponse> stockBajo() {
        return listar("/api/productos/stock-bajo");
    }

    public List<ProductoResponse> agotados() {
        return listar("/api/productos/agotados");
    }

    public List<ProductoResponse> vencidos() {
        return listar("/api/productos/vencidos");
    }

    public List<ProductoResponse> proximosAVencer() {
        return listar("/api/productos/proximos-a-vencer");
    }

    public List<String> alertas() {
        return cliente.get().uri("/api/productos/alertas").retrieve()
                .body(new ParameterizedTypeReference<>() { });
    }

    private List<ProductoResponse> listar(String ruta) {
        return cliente.get().uri(ruta).retrieve()
                .body(new ParameterizedTypeReference<>() { });
    }
}
