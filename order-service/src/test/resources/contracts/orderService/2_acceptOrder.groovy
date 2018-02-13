org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'PUT'
    url $(regex('/order/[0-9]{5}'))
  }
response {
  status 200
  body([
    id: fromRequest().path(0),
    status: 'DONE',
    productIds: ['1','3'],
    customerId: '1',
    accountId: '1',
    price: 1000
    ])
  headers {
    contentType(applicationJson())
  }
 }
}