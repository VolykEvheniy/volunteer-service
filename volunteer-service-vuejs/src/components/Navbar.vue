<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <!--    Logo-->
    <router-link class="navbar-brand" :to="{ name: 'Home' }">
      <img id="logo" width="auto" height="48" src="../assets/air.png" />
    </router-link>

    <!-- Button-->
    <button
      class="navbar-toggler"
      type="button"
      data-toggle="collapse"
      data-target="#navbarSupportedContent"
      aria-controls="navbarSupportedContent"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <!--      Search Bar-->
      <form class="form-inline ml-auto mr-auto">
        <div class="input-group">
          <input
            size="35"
            type="text"
            class="form-control"
            placeholder="Пошук"
            aria-label="Username"
            aria-describedby="basic-addon1"
          />
          <div class="input-group-prepend">
            <span class="input-group-text" id="search-button-navbar">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                fill="currentColor"
                class="bi bi-search"
                viewBox="0 0 16 16"
              >
                <path
                  d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
                />
              </svg>
            </span>
          </div>
        </div>
      </form>

      <div v-if="username" class="navbar-text text-light">
        Вітаємо, {{ username }}!
      </div>
      <!--      DropDowns-->
      <ul class="navbar-nav ml-auto">
        <li class="nav-item dropdown">
          <a
            class="nav-link text-light dropdown-toggle"
            href="#"
            id="navbarDropdown"
            role="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Меню
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Home' }"
              >Головна</router-link
            >
            <router-link class="dropdown-item" :to="{ name: 'Product' }"
              >Товари</router-link
            >
            <router-link class="dropdown-item" :to="{ name: 'Category' }"
              >Категорії</router-link
            >
            <router-link
                class="dropdown-item"
                v-if="!token"
                :to="{ name: 'Signin' }"
            >Список бажань</router-link
            >
            <router-link class="dropdown-item" v-else :to="{ name: 'Wishlist' }"
            >Список бажань</router-link
            >
<!--            <router-link class="dropdown-item" :to="{ name: 'Admin' }"-->
<!--            >Admin</router-link-->
<!--            >-->
            <router-link
                class="dropdown-item"
                v-if="!token"
                :to="{ name: 'Signin' }"
            >Увійти</router-link
            >
            <router-link
                class="dropdown-item"
                v-if="!token"
                :to="{ name: 'Signup' }"
            >Зареєструватися</router-link
            >
            <a class="dropdown-item" v-if="token" href="#" @click="signout"
            >Вийти</a
            >
          </div>
        </li>

<!--        <li class="nav-item dropdown">-->
<!--          <a-->
<!--            class="nav-link text-light dropdown-toggle"-->
<!--            href="#"-->
<!--            id="navbarDropdown"-->
<!--            role="button"-->
<!--            data-toggle="dropdown"-->
<!--            aria-haspopup="true"-->
<!--            aria-expanded="false"-->
<!--          >-->
<!--            Аккаунти-->
<!--          </a>-->
<!--          <div class="dropdown-menu" aria-labelledby="navbarDropdown">-->
<!--            <router-link-->
<!--              class="dropdown-item"-->
<!--              v-if="!token"-->
<!--              :to="{ name: 'Signin' }"-->
<!--              >Wishlist</router-link-->
<!--            >-->
<!--            <router-link class="dropdown-item" v-else :to="{ name: 'Wishlist' }"-->
<!--              >Wishlist</router-link-->
<!--            >-->
<!--            <router-link class="dropdown-item" :to="{ name: 'Admin' }"-->
<!--              >Admin</router-link-->
<!--            >-->
<!--            <router-link-->
<!--              class="dropdown-item"-->
<!--              v-if="!token"-->
<!--              :to="{ name: 'Signin' }"-->
<!--              >Log In</router-link-->
<!--            >-->
<!--            <router-link-->
<!--              class="dropdown-item"-->
<!--              v-if="!token"-->
<!--              :to="{ name: 'Signup' }"-->
<!--              >Sign Up</router-link-->
<!--            >-->
<!--            <a class="dropdown-item" v-if="token" href="#" @click="signout"-->
<!--              >Sign Out</a-->
<!--            >-->
<!--          </div>-->
<!--        </li>-->

        <li class="nav-item">
          <router-link class="nav-link text-light" :to="{ name: 'Order' }"
            >Замовлення</router-link
          >
        </li>
        <li class="nav-item">
          <div id="cart">
            <span id="nav-cart-count">{{ cartCount }}</span>
            <router-link class="text-light" :to="{ name: 'Cart' }"
              ><i class="fa fa-shopping-cart" style="font-size:36px"></i
            ></router-link>
          </div>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: "Navbar",
  props: ["cartCount"],
  data() {
    return {
      token: null,
      username: null,
    };
  },
  methods: {
    signout() {
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      this.token = null;
      this.$emit("resetCartCount");
      this.$router.push({ name: "SignInToken" });
      swal({
        text: "Вихід із системи успішно виконано",
        icon: "success",
        closeOnClickOutside: false,
      });
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.username = localStorage.getItem("username");
  },
};
</script>

<!--<style scoped>-->
<!--#logo {-->
<!--  /*width: 150px;*/-->
<!--  margin-left: 20px;-->
<!--  margin-right: 20px;-->
<!--}-->

<!--.nav-link {-->
<!--  color: rgb(255, 255, 255);-->
<!--}-->

<!--#search-button-navbar {-->
<!--  background-color: #febd69;-->
<!--  border-color: #febd69;-->
<!--  border-top-right-radius: 2px;-->
<!--  border-bottom-right-radius: 2px;-->
<!--}-->
<!--#nav-cart-count {-->
<!--  background-color: red;-->
<!--  color: white;-->
<!--  border-radius: 50%;-->
<!--  position: absolute;-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: center;-->
<!--  width: 15px;-->
<!--  height: 15px;-->
<!--  font-size: 15px;-->
<!--  margin-left: 10px;-->
<!--}-->
<!--#cart {-->
<!--  position: relative;-->
<!--}-->
<!--</style>-->
<style scoped>
.navbar {
  background-color: #202020;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
}

#logo {
  margin-left: 20px;
  margin-right: 20px;
  filter: brightness(0) invert(1);
}

.nav-link {
  color: #febd69;
}

.navbar-toggler {
  border-color: #febd69;
  width: 30px;
  height: 24px;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba(254, 189, 105, 1)' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}

#search-button-navbar {
  background-color: #febd69;
  border-color: #febd69;
}

#nav-cart-count {
  background-color: red;
  color: white;
  border-radius: 50%;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 15px;
  height: 15px;
  font-size: 15px;
  margin-left: 10px;
}

#cart {
  position: relative;
}
</style>
