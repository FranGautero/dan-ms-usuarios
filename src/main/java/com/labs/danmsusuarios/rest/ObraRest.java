package com.labs.danmsusuarios.rest;



import com.labs.danmsusuarios.model.Obra;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
@RequestMapping("/api/obra")
@Api(value = "ObraRest", description = "Permite gestionar las Obras de la empresa")
public class ObraRest {

    private static final List<Obra> listaObras = new ArrayList<>();
    private static Integer ID_GEN = 1;

    //POST Obra nueva

    @PostMapping
    public ResponseEntity<Obra> crear(@RequestBody Obra nuevo){
        System.out.println(" crear Obra "+nuevo);
        nuevo.setId(ID_GEN++);
        listaObras.add(nuevo);
        return ResponseEntity.ok(nuevo);
    }

    // PUT Actualizar Obra por id

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza una Obra")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actualizado correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")
    })
    public ResponseEntity<Obra> actualizar(@RequestBody Obra nuevo, @PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaObras.size())
                .filter(i -> listaObras.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaObras.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //DETELE una Obra

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Obra> borrar(@PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaObras.size())
                .filter(i -> listaObras.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaObras.remove(indexOpt.getAsInt());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //GET Obra por ID

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un Obra por id")
    public ResponseEntity<Obra> ObraPorId(@PathVariable Integer id){

        Optional<Obra> c =  listaObras
                .stream()
                .filter(unCli -> unCli.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }

    //GET Obra por cuit cliente

    @GetMapping(path = "/clienteobra")
    @ApiOperation(value = "Busca un Empleado por id")
    public ResponseEntity<List<Obra>> obraPorCuitCliente(@RequestParam(required = false) String cuit, @RequestParam(required = false) String desc){

        List<Obra> c;

        if(desc == null) {
             c = listaObras
                    .stream()
                    .filter(unCli -> unCli.getCliente().getCuit().equals(cuit)).collect(Collectors.toList());

        }else if(cuit == null){
             c = listaObras
                    .stream()
                    .filter(unCli -> unCli.getTipo().getDescripcion().equals(desc)).collect(Collectors.toList());

        }else{
             c = listaObras
                    .stream()
                    .filter(unCli -> unCli.getTipo().getDescripcion().equals(desc) && unCli.getCliente().getCuit().equals(cuit)).collect(Collectors.toList());
        }
        return ResponseEntity.ok(c);
    }


    @GetMapping
    public ResponseEntity<List<Obra>> todos(){
        return ResponseEntity.ok(listaObras);
    }


}
