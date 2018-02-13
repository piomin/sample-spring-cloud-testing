org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'POST'
    url '/order'
    body([
      status: 'NEW',
      productIds: ['1','3'],
      customerId: '1'
    ])
    headers {
      contentType('application/json')
    }
  }
response {
  status 200
  body([
    id: $(regex('[0-9]{5}')),
    status: 'ACCEPTED',
    productIds: ['1','3'],
    customerId: '1',
    accountId: '1',
    price: 950
  ])
  headers {
    contentType('application/json')
  }
 }
}