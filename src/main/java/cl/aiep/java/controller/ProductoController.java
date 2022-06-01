package cl.aiep.java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cl.aiep.java.exceptions.ProductoNoEncontradoException;
import cl.aiep.java.model.Producto;
import cl.aiep.java.repository.ProductoRepository;

@RestController
@CrossOrigin("*")//evitar problemas con cors
public class ProductoController {
	//inyeccion de repositorio
	@Autowired
	private ProductoRepository productoRepository;
	
	//lista todos los productos
	@GetMapping("/productos")
	public List<Producto> todosLosProductos(){
		return productoRepository.findAll();
	}
	
	//Crea un nuevo producto
	@PostMapping("/productos")
	public Producto nuevoProducto(@RequestBody Producto productoNuevo) {
		return productoRepository.saveAndFlush(productoNuevo);
	}
	
	//Busca el producto por su id
	@GetMapping("/productos/{id}")
	public Producto productoPorId(@PathVariable Long id) {
		return productoRepository.findById(id)
				.orElseThrow(() -> new ProductoNoEncontradoException(id) )
				;
	}
	
	//actualiza el producto con su id
	@PutMapping("/productos/{id}")
	public Producto reemplazarProducto(@RequestBody Producto productoAReemplazar, @PathVariable Long id) {
		return productoRepository.findById(id)
				.map(producto -> {
					producto.setNombre(productoAReemplazar.getNombre());
					producto.setPrecio(productoAReemplazar.getPrecio());
					return productoRepository.save(producto);
				})
				.orElseGet(() -> {
					productoAReemplazar.setId(id);
					return productoRepository.save(productoAReemplazar);
				})
				;
	}
	
	//borrar el producto segun id
	@DeleteMapping("/productos/{id}")
	public void eliminarProducto(@PathVariable Long id) {
		productoRepository.deleteById(id);
	}
	
	
}	
