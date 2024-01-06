<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-4 col-12">
        <img :src="product.imageURL" :alt="product.name" class="img-fluid" />
      </div>
      <div class="col-md-6 col-12 pt-3 pt-md-0">
        <h4>{{ product.name }}</h4>
        <h6 class="category font-italic">{{ category.categoryName }}</h6>
        <p>
          {{ product.description }}
        </p>

        <div class="d-flex flex-row justify-content-between">
          <div class="input-group col-md-5 col-6">
            <div class="input-group-prepend">
              <span class="input-group-text" id="basic-addon1">Кількість</span>
            </div>
            <input class="form-control" type="number" v-model="quantity" min="1" />
          </div>

          <div class="btn-group-vertical ml-3" role="group" aria-label="Button group">
            <button
                type="button"
                id="add-to-cart-button"
                class="btn btn-block"
                @click="addToCart(this.id)"
            >
              Додати в кошик
              <ion-icon name="cart-outline" v-pre></ion-icon>
            </button>

            <button
                id="wishlist-button"
                class="btn btn-block mt-2"
                :class="{ product_added_wishlist: isAddedToWishlist }"
                @click="addToWishList(this.id)"
            >
              {{ wishlistString }}
            </button>
            <button
                id="show-cart-button"
                type="button"
                class="btn btn-block mt-2"
                @click="listCartItems()"
            >
              Показати кошик
              <ion-icon name="cart-outline" v-pre></ion-icon>
            </button>
          </div>
        </div>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      product: {},
      category: {},
      id: null,
      token: null,
      isAddedToWishlist: false,
      wishlistString: "Додати до списку бажань",
      quantity: 1,
    };
  },
  props: ["baseURL", "products", "categories"],
  methods: {
    addToWishList(productId) {
      axios
        .post(`${this.baseURL}wishlist/add?token=${this.token}`, {
          id: productId,
        })
        .then(
          (response) => {
            if (response.status == 201) {
              this.isAddedToWishlist = true;
              this.wishlistString = "Товар додано до списку бажань";
            }
          },
          (error) => {
            console.log(error);
          }
        );
    },
    addToCart(productId) {
      if (!this.token) {
        swal({
          text: "Ввійдіть спочатку, будь ласка!",
          icon: "error",
        });
        return;
      }
      axios
        .post(`${this.baseURL}cart/add?token=${this.token}`, {
          productId: productId,
          quantity: this.quantity,
        })
        .then(
          (response) => {
            if (response.status == 201) {
              swal({
                text: "Товар додано до кошику!",
                icon: "success",
                closeOnClickOutside: false,
              });
              // refresh nav bar
              this.$emit("fetchData");
            }
          },
          (error) => {
            console.log(error);
          }
        );
    },

    listCartItems() {
      axios.get(`${this.baseURL}cart/?token=${this.token}`).then(
        (response) => {
          if (response.status === 200) {
            this.$router.push("/cart");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
  mounted() {
    this.id = this.$route.params.id;
    this.product = this.products.find((product) => product.id == this.id);
    this.category = this.categories.find(
      (category) => category.id == this.product.categoryId
    );
    this.token = localStorage.getItem("token");
  },
};
</script>

<style>
.category {
  font-weight: 400;
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"] {
  -moz-appearance: textfield;
}

#add-to-cart-button {
  background-color: #febd69;
}

#wishlist-button {
  background-color: #b9b9b9;
  border-radius: 0;
}

#show-cart-button {
  background-color: #131921;
  color: white;
  border-radius: 0;
}

.btn-block {
  width: 100%;
}
</style>
