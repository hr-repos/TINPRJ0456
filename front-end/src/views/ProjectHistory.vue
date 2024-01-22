<template>
    <div>
        <div class="input-container">
            <DateTimeRangePicker/>
        </div>

        <!-- Existing LineChart for inputNH3 and outputNH3 -->
        <LineChart :inputNH3="inputNH3" :outputNH3="outputNH3" :min="min" :max="max"/>

        <!-- New HistoryChart for database values -->
        <HistoryChart
            v-if="activeProject"
            :databaseValues="databaseValues"
            :min="minDatabase"
            :max="maxDatabase"
        />

        <div v-if="activeProject">
            <h2>Active Project Information</h2>
            <p>Project ID: {{ activeProject.id }}</p>
            <p>Project Name: {{ activeProject.name }}</p>
        </div>
        <ul>
            <li v-for="project in projectHistory" :key="project.id">
                {{ project.name }} - {{ project.date }}
            </li>
        </ul>
    </div>
</template>

<script>
import DateTimeRangePicker from '../components/DateTimePicker.vue'
import LineChart from '../components/LineChart.vue'
import HistoryChart from '../components/HistoryChart.vue'

export default {
    components: {
        DateTimeRangePicker,
        LineChart,
        HistoryChart
    },
    data()
    {
        return {
            inputNH3: [], // Replace with actual data
            outputNH3: [], // Replace with actual data
            min: 0,
            max: 100,
            activeProject: null,
            projectHistory: [],
            projects: [],
            databaseValues: [],
            minDatabase: 0,
            maxDatabase: 100
        }
    },
    methods: {
        async fetchActiveProject(projectId)
        {
            try
            {
                const response = await fetch(`/api/get-active-project/${projectId}`)
                const data = await response.json()
                this.activeProject = data

                if (this.activeProject)
                {
                    this.projectHistory.unshift({
                        id: this.activeProject.id,
                        name: this.activeProject.name,
                        date: new Date().toLocaleString()
                    })
                }
            } catch (error)
            {
                console.error('Error fetching active project:', error)
            }
        },
        async fetchAndAddToHistory(projectId)
        {
            await this.fetchActiveProject(projectId)
            await this.fetchDatabaseValues()
        },
        async fetchDatabaseValues()
        {
            if (this.activeProject && this.activeProject.id)
            {
                try
                {
                    const response = await fetch(`/api/get-database-values/${this.activeProject.id}`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            beginDate: this.activeProject.beginDate,
                            endDate: this.activeProject.endDate
                        })
                    })

                    const data = await response.json()

                    this.databaseValues = data.values
                    this.minDatabase = data.min
                    this.maxDatabase = data.max
                } catch (error)
                {
                    console.error('Error fetching database values:', error)
                }
            }
        }
    },
    created()
    {
        fetch('/api/get-projects')
            .then((response) => response.json())
            .then((data) =>
            {
                this.projects = data
            })
    }
}
</script>

<style scoped>
.input-container {
    display: flex;
    justify-content: space-between;
}
</style>
