org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'POST'
    url '/product'
    body([
      ['1'],
      ['4']
    ])
  }
response {
  status 200
  body([
    [
      id: $(regex('[0-9]{1}')),
      name: $(regex('[a-z][A-Z][0-9]{5}')),
      price: $(regex('[0-9]{3}'))
    ], [
	  id: $(regex('[0-9]{1}')),
      name: $(regex('[a-z][A-Z][0-9]{5}')),
      price: $(regex('[0-9]{3}'))
	]
  ])
  headers {
    contentType(applicationJson())
  }
 }
}