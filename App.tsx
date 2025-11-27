import React from 'react';
import { SafeAreaView, StatusBar, StyleSheet, Text, View, TouchableOpacity } from 'react-native';

const App = () => {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <View style={styles.content}>
        <Text style={styles.logo}>🚀</Text>
        <Text style={styles.title}>NativeHub</Text>
        <Text style={styles.subtitle}>Advanced React Native Showcase</Text>
        
        <View style={styles.features}>
          <FeatureCard icon="🔐" title="Biometric Auth" />
          <FeatureCard icon="📊" title="Performance Monitor" />
          <FeatureCard icon="🔒" title="Secure Storage" />
          <FeatureCard icon="⚡" title="Native Modules" />
        </View>
      </View>
    </SafeAreaView>
  );
};

const FeatureCard = ({ icon, title }: { icon: string; title: string }) => (
  <View style={styles.card}>
    <Text style={styles.cardIcon}>{icon}</Text>
    <Text style={styles.cardTitle}>{title}</Text>
  </View>
);

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  content: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 24 },
  logo: { fontSize: 64, marginBottom: 16 },
  title: { fontSize: 32, fontWeight: '800', color: '#0F172A' },
  subtitle: { fontSize: 16, color: '#64748B', marginBottom: 32 },
  features: { flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'center', gap: 16 },
  card: { width: 140, padding: 16, backgroundColor: '#FFF', borderRadius: 12, alignItems: 'center',
    shadowColor: '#000', shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.1, shadowRadius: 4, elevation: 3 },
  cardIcon: { fontSize: 32, marginBottom: 8 },
  cardTitle: { fontSize: 14, fontWeight: '600', color: '#0F172A', textAlign: 'center' },
});

export default App;
