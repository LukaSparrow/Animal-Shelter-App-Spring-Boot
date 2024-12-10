package studia.animalshelterapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import studia.animalshelterapp.DTO.AnimalDTO;
import studia.animalshelterapp.DTO.AnimalShelterDTO;
import studia.animalshelterapp.DTO.RatingDTO;
import studia.animalshelterapp.controllers.ShelterController;
import studia.animalshelterapp.controllers.AnimalController;
import studia.animalshelterapp.controllers.RatingController;
import studia.animalshelterapp.models.AnimalCondition;
import studia.animalshelterapp.services.AnimalService;
import studia.animalshelterapp.services.RatingService;
import studia.animalshelterapp.services.ShelterService;
import studia.animalshelterapp.exceptions.*;
import studia.animalshelterapp.models.Animal;
import studia.animalshelterapp.models.AnimalShelter;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationControllersTest {

	private MockMvc mockMvcShelter;
	private MockMvc mockMvcAnimal;
	private MockMvc mockMvcRating;

	@Mock
	private AnimalService animalService;

	@Mock
	private ShelterService shelterService;

	@Mock
	private RatingService ratingService;

	@InjectMocks
	private ShelterController shelterController;

	@InjectMocks
	private AnimalController animalController;

	@InjectMocks
	private RatingController ratingController;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		mockMvcShelter = MockMvcBuilders.standaloneSetup(shelterController).build();
		mockMvcAnimal = MockMvcBuilders.standaloneSetup(animalController).build();
		mockMvcRating = MockMvcBuilders.standaloneSetup(ratingController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testAddAnimal_Success() throws Exception {
		AnimalDTO animalDTO = new AnimalDTO("Dog", "Bulldog", 3, 150.0, AnimalCondition.ZDROWE, 1L);
		Animal animal = new Animal("Dog", "Bulldog", AnimalCondition.ZDROWE, 3, 150.0);
		when(animalService.addAnimal(any(AnimalDTO.class))).thenReturn(animal);

		mockMvcAnimal.perform(post("/api/animal")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(animalDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.animalName").value("Dog"))
				.andExpect(jsonPath("$.animalSpecies").value("Bulldog"));
	}

	@Test
	public void testAddAnimal_ShelterNotFound() throws Exception {
		AnimalDTO animalDTO = new AnimalDTO("Dog", "Bulldog", 3, 150.0, AnimalCondition.ZDROWE, 1L);
		when(animalService.addAnimal(any(AnimalDTO.class))).thenThrow(new ShelterNotFoundException("Shelter with id 1 not found."));

		mockMvcAnimal.perform(post("/api/animal")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(animalDTO)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Shelter not found."))
				.andExpect(jsonPath("$.details").value("Shelter with id 1 not found."));
	}

	@Test
	public void testDeleteAnimal_Success() throws Exception {
		doNothing().when(animalService).deleteAnimal(1L);

		mockMvcAnimal.perform(delete("/api/animal/{id}", 1L))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteAnimal_AnimalNotFound() throws Exception {
		doThrow(new AnimalNotFoundException("Animal with id 1 not found.")).when(animalService).deleteAnimal(1L);

		mockMvcAnimal.perform(delete("/api/animal/{id}", 1L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Animal not found."))
				.andExpect(jsonPath("$.details").value("Animal with id 1 not found."));
	}

	@Test
	public void testGetAnimal_Success() throws Exception {
		Animal animal = new Animal("Dog", "Bulldog", AnimalCondition.ZDROWE, 3, 150.0);
		when(animalService.getAnimalById(1L)).thenReturn(animal);

		mockMvcAnimal.perform(get("/api/animal/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.animalName").value("Dog"))
				.andExpect(jsonPath("$.animalSpecies").value("Bulldog"));
	}

	@Test
	public void testGetAnimal_AnimalNotFound() throws Exception {
		when(animalService.getAnimalById(1L)).thenThrow(new AnimalNotFoundException("Animal with id 1 not found."));

		mockMvcAnimal.perform(get("/api/animal/{id}", 1L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Animal not found."))
				.andExpect(jsonPath("$.details").value("Animal with id 1 not found."));
	}

	@Test
	public void testGetShelterCSV_Success() throws Exception {
		String csvData = "Shelter1,10,20,4.5";
		when(shelterService.generateShelterCSV()).thenReturn(csvData);

		mockMvcShelter.perform(get("/api/animalshelter/csv"))
				.andExpect(status().isOk())
				.andExpect(content().string(csvData));
	}

	@Test
	public void testGetShelterCSV_ShelterNotFound() throws Exception {
		when(shelterService.generateShelterCSV()).thenThrow(new ShelterNotFoundException("There are no shelters."));

		mockMvcShelter.perform(get("/api/animalshelter/csv"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Shelters not found."))
				.andExpect(jsonPath("$.details").value("There are no shelters."));
	}

	@Test
	public void testAddShelter_Success() throws Exception {
		AnimalShelterDTO shelterDTO = new AnimalShelterDTO("Shelter1", 20);
		AnimalShelter shelter = new AnimalShelter("Shelter1", 20);
		when(shelterService.addShelter(any(AnimalShelterDTO.class))).thenReturn(shelter);

		mockMvcShelter.perform(post("/api/animalshelter")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(shelterDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.shelterName").value("Shelter1"))
				.andExpect(jsonPath("$.maxCapacity").value(20));
	}

	@Test
	public void testAddShelter_ShelterAlreadyExists() throws Exception {
		AnimalShelterDTO shelterDTO = new AnimalShelterDTO("Shelter1", 20);
		when(shelterService.addShelter(any(AnimalShelterDTO.class))).thenThrow(new ShelterAlreadyExistsException("Shelter of provided name already exists."));

		mockMvcShelter.perform(post("/api/animalshelter")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(shelterDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Shelter of said name already exists."))
				.andExpect(jsonPath("$.details").value("Shelter of provided name already exists."));
	}

	@Test
	public void testDeleteShelter_Success() throws Exception {
		doNothing().when(shelterService).deleteShelter(1L);

		mockMvcShelter.perform(delete("/api/animalshelter/{id}", 1L))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteShelter_ShelterNotFound() throws Exception {
		doThrow(new ShelterNotFoundException("Shelter with id 1 not found.")).when(shelterService).deleteShelter(1L);

		mockMvcShelter.perform(delete("/api/animalshelter/{id}", 1L))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Shelters not found."))
				.andExpect(jsonPath("$.details").value("Shelter with id 1 not found."));
	}

	@Test
	public void testGetAllShelters_Success() throws Exception {
		AnimalShelter shelter = new AnimalShelter("Shelter1", 20);
		when(shelterService.getAllShelters()).thenReturn(List.of(shelter));

		mockMvcShelter.perform(get("/api/animalshelter"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].shelterName").value("Shelter1"))
				.andExpect(jsonPath("$[0].maxCapacity").value(20));
	}

	@Test
	public void testAddRating_Success() throws Exception {
		RatingDTO ratingDTO = new RatingDTO(1L, "Great shelter!", 5);
		doNothing().when(ratingService).addRating(any(RatingDTO.class));

		mockMvcRating.perform(post("/api/rating")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(ratingDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void testAddRating_InvalidRating() throws Exception {
		RatingDTO ratingDTO = new RatingDTO(1L, "Great shelter!", 6);

		// Poprawne mockowanie metody void
		doThrow(new IllegalArgumentException("Rating value must be in range of 1-5.")).when(ratingService).addRating(any(RatingDTO.class));

		mockMvcRating.perform(post("/api/rating")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(ratingDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid rating value."))
				.andExpect(jsonPath("$.details").value("Rating value must be in range of 1-5."));
	}

	@Test
	public void testAddRating_ShelterNotFound() throws Exception {
		RatingDTO ratingDTO = new RatingDTO(1L, "Great shelter!", 5);

		// Poprawne mockowanie metody void
		doThrow(new ShelterNotFoundException("Shelter with id 1 not found.")).when(ratingService).addRating(any(RatingDTO.class));

		mockMvcRating.perform(post("/api/rating")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(ratingDTO)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Shelter not found."))
				.andExpect(jsonPath("$.details").value("Shelter with id 1 not found."));
	}
}