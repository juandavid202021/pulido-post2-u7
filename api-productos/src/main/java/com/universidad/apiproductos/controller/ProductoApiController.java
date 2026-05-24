package com.universidad.apiproductos.controller;

import com.universidad.apiproductos.model.Producto;
import com.universidad.apiproductos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoApiController {

    @Autowired
    private ProductoService servicio;

    // GET /api/productos -> Retorna todos los productos en formato JSON
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    // GET /api/productos/{id} -> Retorna un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    // POST /api/productos -> Crea un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevo = servicio.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // PUT /api/productos/{id} -> Actualiza un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        return servicio.buscarPorId(id)
                .map(existente -> {
                    producto.setId(id);
                    return ResponseEntity.ok(servicio.guardar(producto));
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    // DELETE /api/productos/{id} -> Elimina un producto existente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (servicio.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Producto no encontrado: " + id);
        }

        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}