# Basket

Basket is in charge of managing basket services for the e-commerce "felicisimo".
Basket should provide and scalable and elastic service to have the ability of 
support customer demand.


## Assumptions

Supposing [this architecture], following assumptions should be considered:

 - The anonymous basket, discounts and catalog items is out of the scope of this 
  version: basket does not manage discounts neither catalog items.
 - catalog service will provide info about items
 - marketing service will provide info discounts

## Requirements

### Product

Basket must provide following endpoints:

 - create basket: `{userId}/basket/`
 - get/delete basket: GET/DELETE `{userId}/basket/{id}`
 - add/remove element by id: POST/DELETE `{userId}/basket/{id}/item/{id}`

### Devs

Scala + akka + ? + docker + helm + jenkins

### Ops

Easily to build and deploy.

## Getting started

## Test

## Docker

## Helm

## CI/CD (jenkins)

## Author

## License


 
 


