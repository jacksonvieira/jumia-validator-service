package com.jumia.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumia.validator.customer.domain.Country;
import com.jumia.validator.customer.domain.Customer;
import com.jumia.validator.customer.service.CustomerService;
import com.jumia.validator.lib.properties.JumiaProperties;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application.test.properties")
public class ValidatePhoneNumberTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JumiaProperties jumiaProperties;

	@Autowired
	private CustomerService service;

	@Test
	public void test() throws Exception {
		MvcResult result = mockMvc.perform(get("/customers").contentType(contentType)).andExpect(status().isOk())
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		List<Customer> customers = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Customer>>() {
				});

		customers.forEach((customer) -> validatePhoneNumbers(customer));
	}

	private void validatePhoneNumbers(Customer customer) {
		// preciso saber se onde que é esse customer
		Integer countryCode = service.tryToExtractCountry(customer.getPhone());
		if (null != countryCode) {
			for (Country country : Country.values()) {
				if (country.getCountryCode().equals(countryCode)) {
					System.out.println(country.toString());
					System.out.println("phone: " + customer.getPhone());
					System.out.println("É valido? " + service.validatePhone(country, customer.getPhone()));
				}
			}
		}
	}

	@Test
	public void testAReturnAllCustomer() throws Exception {
		MvcResult result = mockMvc.perform(get("/customers").contentType(contentType)).andExpect(status().isOk())
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		List<Customer> customers = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Customer>>() {
				});

		assertThat(customers, not(IsEmptyCollection.empty()));

	}

	@Test
	public void testBReturnSingleCustomer() throws Exception {
		MvcResult result = mockMvc.perform(get("/customers/34").contentType(contentType)).andExpect(status().isOk())
				.andReturn();
		ObjectMapper mapper = new ObjectMapper();
		Customer customer = mapper.readValue(result.getResponse().getContentAsString(), Customer.class);
		assertThat(customer.getId(), is(34l));
		assertThat(customer.getName(), is("LOUIS PARFAIT OMBES NTSO"));
		assertThat(customer.getPhone(), is("(237) 673122155"));
	}

	@Test
	public void testCGetANonExistentCustomer() throws Exception {
		mockMvc.perform(get("/customers/33").contentType(contentType)).andExpect(status().isNoContent());
	}

	// @Test
	public void testDCreateACustomer() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Customer customer = new Customer();
		customer.setName("Jackson Vieira");
		customer.setPhone("(55) 48996345146");
		String json = mapper.writeValueAsString(customer);
		MvcResult result = mockMvc.perform(post("/customers").contentType(contentType).content(json))
				.andExpect(status().isCreated()).andReturn();

		assertThat(result.getResponse().containsHeader("Location"), is(true));
		assertThat(result.getResponse().getHeader("Location"), is(jumiaProperties.getUrl() + "/customers/3"));
	}

	// @Test
	public void testECreateACWithoutData() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Customer customer = new Customer();
		String json = mapper.writeValueAsString(customer);

		mockMvc.perform(post("/customers").contentType(contentType).content(json))
				.andExpect(status().isInternalServerError()).andReturn();
	}

	@Test
	public void testFEditCustomerWithPut() throws Exception {
		Customer mockCustomer = new Customer(19l);
		mockCustomer.setName("Guilherme");
		mockCustomer.setPhone("(55) 48996345146");
		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(mockCustomer);
		MvcResult result = mockMvc.perform(put("/customers/19").contentType(contentType).content(json))
				.andExpect(status().isOk()).andReturn();

		Customer customer = mapper.readValue(result.getResponse().getContentAsString(), Customer.class);

		assertThat(customer.getId(), is(19l));
		assertThat(customer.getName(), is("Guilherme"));
		assertThat(customer.getPhone(), is("(55) 48996345146"));
	}

	// @Test
	public void testGEditCustomerWithoutRequiredArgsWithPut() throws Exception {
		Customer mockCustomer = new Customer(19l);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(mockCustomer);
		mockMvc.perform(put("/customers/19").contentType(contentType).content(json)).andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	public void testHEditNonExistentCustomerWithPut() throws Exception {
		Customer mockCustomer = new Customer(33l);
		mockCustomer.setName("Jaco");

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(mockCustomer);
		mockMvc.perform(put("/customers/33").contentType(contentType).content(json)).andExpect(status().isNoContent());
	}

	@Test
	public void testIEditCustomerWithPatch() throws Exception {
		Customer mockCustomer = new Customer(19l);
		mockCustomer.setName("Edit with patch");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(mockCustomer);
		MvcResult result = mockMvc.perform(patch("/customers/19").contentType(contentType).content(json))
				.andExpect(status().isOk()).andReturn();

		Customer customer = mapper.readValue(result.getResponse().getContentAsString(), Customer.class);
		assertThat(customer.getId(), is(19l));
		assertThat(customer.getName(), is("Edit with patch"));
		assertThat(customer.getPhone(), is("(55) 48996345146"));
	}

	@Test
	public void testJEditNonExistentCustomerWithPatch() throws Exception {
		Customer mockCustomer = new Customer(33l);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(mockCustomer);
		mockMvc.perform(patch("/customers/33").contentType(contentType).content(json))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testKDeleteACustomer() throws Exception {
		this.mockMvc.perform(delete("/customers/19").contentType(contentType)).andExpect(status().isOk());
	}

	@Test
	public void testLDeleteANonExistentCustomer() throws Exception {
		this.mockMvc.perform(delete("/customers/100").contentType(contentType)).andExpect(status().isNoContent());
	}

	@Test
	public void testMValidateNumberFromCameroon() throws Exception {
		String phone = "(237) 691816558";
		assertTrue(service.validatePhone(Country.CAMEROON, phone));
	}

	@Test
	public void testNValidateNumberFromEthiopia() {
		String phone = "(251) 988200000";
		assertTrue(service.validatePhone(Country.ETHIOPIA, phone));

	}

	@Test
	public void testOValidateNumberFromMorocco() {
		String phone = "(212) 691933626";
		assertTrue(service.validatePhone(Country.MOROCCO, phone));
	}

	@Test
	public void testPValidateNumberFromMozambique() {
		String phone = "(258) 847602609";
		assertTrue(service.validatePhone(Country.MOZAMBIQUE, phone));

	}

	@Test
	public void testQValidateNumberFromUganda() {
		String phone = "(256) 714660221";
		assertTrue(service.validatePhone(Country.UGANDA, phone));
	}

}
