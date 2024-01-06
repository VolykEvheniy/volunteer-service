<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h3 class="pt-3">Підтвердіть замовлення</h3>
      </div>
    </div>
    <!-- Order Details -->
    <div class="row">
      <div class="col-md-6">
        <h5>Деталі доставки</h5>
        <form @submit.prevent="submitOrder">
          <div class="form-group">
            <label for="firstName">Ім'я</label>
            <input type="text" class="form-control" id="firstName" v-model="firstName" required>
          </div>
          <div class="form-group">
            <label for="lastName">Прізвище</label>
            <input type="text" class="form-control" id="lastName" v-model="lastName" required>
          </div>
          <div class="form-group">
            <label for="phoneNumber">Номер телефону</label>
            <input type="tel" class="form-control" id="phoneNumber" v-model="phoneNumber" required>
          </div>
          <div class="form-group">
            <label for="address">Повна адреса</label>
            <textarea class="form-control" id="address" v-model="address" required></textarea>
          </div>
          <div class="form-group">
            <label for="postalService">Поштовий сервіс</label>
            <select class="form-control" id="postalService" v-model="postalService" required>
              <option value="">Виберіть поштовий сервіс</option>
              <option value="nova-poshta">Нова пошта</option>
              <option value="ukrposhta">Укрпошта</option>
            </select>
          </div>
          <button type="submit" class="btn btn-primary mt-3">Замовити</button>
        </form>
      </div>
      <div class="col-md-6">
        <h5>Ваше замовлення</h5>
        <ul class="list-group">
          <li class="list-group-item" v-for="item in cartItems" :key="item.product.id">
            <div class="d-flex justify-content-between">
              <div>
                <h6>{{ item.product.name }}</h6>
                <p>Кількість: {{ item.quantity }}</p>
              </div>
              <img v-bind:src="item.product.imageURL" class="embed-responsive-item" style="width: 100px; height: auto;">
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>


<script>
import Swal from "sweetalert2";

const axios = require('axios');
export default {
  name: 'OrderConfirmation',
  props: ['baseURL'],
  data() {
    return {
      firstName: '',
      lastName: '',
      phoneNumber: '',
      address: '',
      postalService: '',
      cartItems: [],
      token: null,
      totalcost: 0,
    };
  },
  methods: {
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

    clearCart() {
      axios
          .delete(`${this.baseURL}cart/clear/?token=${this.token}`)
          .then((response) => {
            if (response.status == 200) {
              this.cartItems = [];
              this.totalCost = 0;
              this.$emit('resetCartCount');
            }
          }, (error) => {
            console.log(error);
          });
    },

    submitOrder() {
      // create order object
      const order = {
        firstName: this.firstName,
        lastName: this.lastName,
        phoneNumber: this.phoneNumber,
        address: this.address,
        postalService: this.postalService,
        cartItems: this.cartItems,
        totalCost: this.totalcost,
      };

      axios.post(`${this.baseURL}order/add`, order, {
        headers:{
          'Authorization': `Bearer ${this.token}`
        }
      }).then((response) => {
        Swal.fire({
          text: `Замовлення прийнято`,
          icon: "success",
          closeOnClickOutside: false,
        }).then(() => {
          Swal.fire({
            title: 'Введіть електронну адресу для надсилання підтвердження',
            input: 'email',
            inputAttributes: {
              autocapitalize: 'off'
            },
            showCancelButton: true,
            confirmButtonText: 'Надіслати',
            cancelButtonText: 'Відмінити',
            showLoaderOnConfirm: true,
            inputValidator: (value) => {
              // Regular expression for basic email validation
              let re = /\S+@\S+\.\S+/;
              if (!re.test(value)) {
                return 'Будь ласка, введіть дійсну електронну адресу';
              }
            },
            preConfirm: (email) => {
              return axios.post(`${this.baseURL}order/email`, {
                email,
                orderId: response.data.orderId,
              }, {
                headers: {
                  'Authorization': `Bearer ${this.token}`
                }
              }).then(response => {
                if (response.status !== 200) {
                  throw new Error('Failed to send email')
                }
                return email;
              }).catch(error => {
                Swal.showValidationMessage(
                    `Request failed: ${error}`
                )
              })
            },
            allowOutsideClick: () => !Swal.isLoading()
          }).then((result) => {
            if (result.isConfirmed) {
              Swal.fire({
                title: `Лист відправлено за адресою ${result.value}`,
                icon: "success",
                closeOnClickOutside: false,
              });
            }
          })
        });
      })
          .catch((err) => {
            console.log(err);
          });
    //},

    // // Submit order
    // submitOrder() {
    //   // create order object
    //   const order = {
    //     firstName: this.firstName,
    //     lastName: this.lastName,
    //     phoneNumber: this.phoneNumber,
    //     address: this.address,
    //     postalService: this.postalService,
    //     cartItems: this.cartItems,
    //     totalCost: this.totalcost,
    //   };
    //   axios.post(`${this.baseURL}order/add`, order, {
    //     headers:{
    //       'Authorization': `Bearer ${this.token}`
    //     }
    //   }).then((response) => {
    //         // display the token
    //         swal({
    //           text: `Замовлення прийнято`,
    //           icon: "success",
    //           closeOnClickOutside: false,
    //         });
    //       })
    //       .catch((err) => {
    //         console.log(err);
    //       });
      //     .then(
      //     (response) => {
      //       if (response.status == 200) {
      //         this.$router.push({ name: 'OrderSuccess' });
      //       }
      //     },
      //     (error) => {
      //       console.log(error);
      //     }
      // );
    },
  },
  mounted() {
    this.token = localStorage.getItem('token');
    this.listCartItems();
  },
};
</script>

<style scoped>
.container {
  font-family: 'Roboto', sans-serif;
}

.form-group label {
  font-weight: bold;
  color: #484848;
}

.btn-primary {
  background-color: #0071ff;
  border: none;
}

h5 {
  border-bottom: 1px solid #232f3e;
  padding-bottom: 10px;
}

.list-group-item {
  border: none;
  background-color: #f8f9fa;
}

.list-group-item .d-flex.justify-content-between h6, .list-group-item .d-flex.justify-content-between p {
  color: #484848;
}
</style>
