# How can you add a new version of your API?

- Create a separate folder for your new version and duplicate everything, API spec, examples, schemas, etc. It's a lot od uplication but it's way easier to manage and to deprecate it when is needed, just delete the folder.
- Add new a OpenAPI yaml file with the proper name: `product-service-api-v2.yaml`
- Deprecate ALL the old API endpoints

      /service-api/v1/products:
        get:
          deprecated: true


