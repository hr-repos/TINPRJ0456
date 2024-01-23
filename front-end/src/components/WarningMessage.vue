<script>
export default {
    props: {
        warning: String,
        description: String,
        link: String
    }
}
</script>

<template>
    <div class="text-center m-7">
        <p class="font-bold text-2xl" v-text="warning"></p>

        <RouterLink class="text-xl" v-if="link" :to="link" v-text="description"></RouterLink>
        <p v-else class="text-xl" v-text="description"></p>
    </div>
</template>
