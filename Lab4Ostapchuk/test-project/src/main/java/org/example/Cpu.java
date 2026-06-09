package org.example;

@ProductModel(fields = {
        @ProductModel.Field(name = "benchmark", type = int.class),
        @ProductModel.Field(name = "socket")
})
public interface Cpu {
}
