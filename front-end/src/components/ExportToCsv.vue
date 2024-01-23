<script>
import { toast } from 'vue3-toastify'

export default {
    props: {
        project: JSON
    },
    methods: {
        async exportCsv() {
            const t = toast.info("Starting download...")
            try {
                const response = await fetch('/api/export-to-csv/' + this.project.id)
                if (response.ok) {
                    const blob = await response.blob()
                    const link = document.createElement('a')

                    link.href = window.URL.createObjectURL(blob)
                    link.download = `export-${this.project.name.toLowerCase().replace(/[^a-z0-9]+/g, '-')}.csv`
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link)
                    toast.remove(t)
                    toast.success("Download complete.")
                } else {
                    const data = await response.json()
                    toast.remove(t)
                    toast.error('Download failed: ' + data.error)
                }
            } catch (error) {
                toast.remove(t)
                toast.error('Download failed: ' + error)
            }
        }
    },
}
</script>

<template>
    <button @click="exportCsv">Export all data to CSV</button>
</template>
