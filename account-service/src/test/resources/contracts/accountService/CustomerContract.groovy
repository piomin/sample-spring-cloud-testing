org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'GET'
    url '/customer/1'
  }
response {
  status 200
  body([
    [
      id: $(regex('[0-9]{5}')),
      number: '123',
      balance: 5000,
      customerId: fromRequest().path(1)
    ], [
	  id: $(regex('[0-9]{5}')),
      number: '124',
      balance: 5000,
      customerId: fromRequest().path(1)
	], [
	  id: $(regex('[0-9]{5}')),
      number: '124',
      balance: 5000,
      customerId: fromRequest().path(1)
	]
  ])
  headers {
    contentType(applicationJson())
  }
 }
}