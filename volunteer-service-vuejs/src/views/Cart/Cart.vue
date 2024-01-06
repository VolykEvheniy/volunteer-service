<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h3 class="pt-3">Кошик для замовлень</h3>
      </div>
    </div>
    <div
        v-for="cartItem in cartItems"
        :key="cartItem.product.id"
        class="row mt-2 pt-3 justify-content-around"
    >
      <div class="col-2"></div>
      <div class="col-md-3 embed-responsive embed-responsive-16by9">
        <router-link
            :to="{ name: 'ShowDetails', params: { id: cartItem.product.id } }"
        >
          <img
              v-bind:src="cartItem.product.imageURL"
              class="w-100 card-img-top embed-responsive-item"
          />
        </router-link>
      </div>
      <div class="col-md-5 px-3">
        <div class="card-block px-3">
          <h6 class="card-title">
            <router-link
                :to="{ name: 'ShowDetails', params: { id: cartItem.product.id } }"
            >{{ cartItem.product.name }}
            </router-link>
          </h6>
          <p id="item-quantity" class="mb-0">
            Кількість :
            <input
                size="1"
                class="p-0 h-25 border-bottom border-top-0 border-left-0 border-right-0"
                v-model="cartItem.quantity"
            />
          </p>
          <br /><a href="#" class="text-right delete-link" @click="deleteItem(cartItem.id)"
        >Видалити з кошика</a
        >
        </div>
      </div>
      <div class="col-2"></div>
      <div class="col-12"><hr /></div>
    </div>
    <div class="total-cost pt-2 text-right">
      <button
          :disabled="isDisabled()"
          type="button"
          class="btn btn-primary confirm"
          @click="checkout"
      >
        Підтвердити замовлення
      </button>
    </div>
  </div>
</template>

<script>
const axios = require('axios');
export default {
  data() {
    return {
      cartItems: [],
      token: null,
      totalcost: 0,
    };
  },
  name: 'Cart',
  props: ['baseURL'],
  methods: {
    isDisabled() {
      if (this.cartItems.length === 0) {
        return true;
      }
      return false;
    },
    // fetch all the items in cart
    listCartItems() {
      axios.get(`${this.baseURL}cart/?token=${this.token}`).then(
        (response) => {
          if (response.status == 200) {
            const result = response.data;
            // store cartitems and total price in two variables
            this.cartItems = result.cartItems;
            this.totalcost = result.totalCost;
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    // go to checkout page
    checkout() {
      this.$router.push({ name: 'Confirmation' });
    },
    deleteItem(itemId) {
      axios
        .delete(`${this.baseURL}cart/delete/${itemId}/?token=${this.token} `)
        .then(
          (response) => {
            if (response.status == 200) {
              this.$router.go(0);
            }
            this.$emit('fetchData');
          },
          (error) => {
            console.log(error);
          }
        );
    },
    showDetails(productId) {
      this.$router.push({
        name: 'ShowDetails',
        params: { id: productId },
      });
    },
  },
  mounted() {
    this.token = localStorage.getItem('token');
    this.listCartItems();
  },
};
</script>

<style scoped>
h3,
h4,
h5 {
  font-family: 'Roboto', sans-serif;
  color: #003366;
  font-weight: 700;
}

.container {
  background-color: #f2f2f2;
  border-radius: 5px;
  padding: 20px;
}

.embed-responsive .card-img-top {
  object-fit: cover;
}

#item-price {
  color: #232f3e;
}

#item-quantity {
  float: left;
}

#item-total-price {
  float: right;
}
.delete-link {
  display: inline-block;
  margin-top: 10px;
  color: #ff0000; /* Red text color */
  text-decoration: none; /* Remove the underline */
  transition: color 0.3s ease; /* Smooth color transition */
}

.delete-link:hover {
  color: #cc0000; /* Darker red color on hover */
}

.confirm {
  font-weight: bold;
  font-size: larger;
  background-color: #003366;
  color: white;
}
</style>
