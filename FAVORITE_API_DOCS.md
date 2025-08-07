### Using Axios

```javascript
import axios from "axios";

// Add to favorites
const addToFavorites = async (userId, movieId) => {
  try {
    const response = await axios.post("/favorites", {
      userId: userId,
      movieId: movieId,
    });
    console.log(response.data); // "Added to favorite list."
  } catch (error) {
    console.error("Error adding to favorites:", error);
  }
};

// Remove from favorites
const removeFromFavorites = async (userId, movieId) => {
  try {
    const response = await axios.delete("/favorites", {
      data: {
        userId: userId,
        movieId: movieId,
      },
    });
    console.log(response.data); // "Removed from favorite list."
  } catch (error) {
    console.error("Error removing from favorites:", error);
  }
};

// Get user favorites
const getUserFavorites = async (userId) => {
  try {
    const response = await axios.get(`/favorites/${userId}`);
    console.log(response.data); // Array of favorite objects
    return response.data;
  } catch (error) {
    console.error("Error getting favorites:", error);
  }
};
```
