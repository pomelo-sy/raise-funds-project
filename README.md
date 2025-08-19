@Configuration
public class SwaggerConfig {
    @Bean
    public ModelConverter resultModelConverter() {
        return new ModelConverter() {
            @Override
            public Schema resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    if (pType.getRawType() == Result.class) {
                        // 如果是 Result<T>，提取 T 的类型
                        Type dataType = pType.getActualTypeArguments()[0];
                        Schema dataSchema = context.resolve(dataType);
                        return new ObjectSchema()
                            .addProperty("code", new IntegerSchema())
                            .addProperty("message", new StringSchema())
                            .addProperty("data", dataSchema);
                    }
                }
                return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
            }
        };
    }
}
