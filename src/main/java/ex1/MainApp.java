package ex1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainApp {

    public static List<Carte> citireInList() {
        try {
            File file=new File("src/main/resources/carti.json");
            ObjectMapper mapper=new ObjectMapper();
           // mapper.registerModule(new JavaTimeModule());
            List<Carte> carti = mapper.readValue(file, new TypeReference<List<Carte>>(){});
            return carti;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Integer,Carte> citireInMap() {
        try {
            //File file=new File("src/main/resources/carti_map.json");
            File file=new File("src/main/resources/carti_modificate.json");
            ObjectMapper mapper=new ObjectMapper();
            //mapper.registerModule(new JavaTimeModule());
            Map<Integer,Carte> carteMap = mapper.readValue(file, new TypeReference<Map<Integer,Carte>>(){});
            return carteMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void scriereInMap(Map<Integer,Carte> carteMap) {
        try {
            ObjectMapper mapper=new ObjectMapper();
            File file=new File("src/main/resources/carti_modificate.json");
            mapper.writeValue(file,carteMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void afisareLista(List<Carte> carteList){
        for(Carte c:carteList){
            System.out.println(c);
        }
    }

    public static void afisareMap(Map<Integer,Carte> carteMap){
        for (Map.Entry<Integer, Carte> entry : carteMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        /*List<Carte> list=new ArrayList<Carte>();
        Carte c=new Carte("asd","zxc",2003);
        list.add(c);
        afisareLista(list);*/
        //List<Carte> carteList=citireInList();
        //afisareLista(carteList);

        Map<Integer,Carte> carteMap = citireInMap();
        Scanner sc=new Scanner(System.in);
        int opt=0;
        do{
            System.out.println("0=iesire");
            System.out.println("1=afisare");
            System.out.println("2=stergere");
            System.out.println("3=adaugare");
            System.out.println("4=salvare in json");
            System.out.println("5=extragere autor");
            System.out.println("6=sortare carti Yual");
            System.out.println("7=datele celei mai vechi cărți");
            opt=sc.nextInt();
            switch (opt){
                case 0->{
                    System.out.println("iesire din program");
                }
                case 1->{
                    afisareMap(carteMap);
                }
                case 2->{
                    stergereCarte(carteMap);
                }
                case 3->{
                    adaugareCarte(carteMap);
                }
                case 4->{
                    scriereInMap(carteMap);
                }
                case 5->{
                    Set<Carte> cartiYual=extragereAutor(carteMap);

                }
                case 6->{
                    Set<Carte> cartiYual = extragereAutor(carteMap);
                    System.out.println();

                    Set<Carte> sortedCarti = cartiYual.stream()
                            .sorted(Comparator.comparing(Carte::titlul))
                            .collect(Collectors.toCollection(LinkedHashSet::new));

                    sortedCarti.forEach(System.out::println);
                }
                case 7->{
                    dateleCeleiMaiVechiCarti(carteMap);
                }
                default -> {
                    System.out.println("eroare");
                }
            }

        }while(opt!=0);

    }

    private static void dateleCeleiMaiVechiCarti(Map<Integer, Carte> carteMap) {
        Optional<Map.Entry<Integer, Carte>> celMaiVecCarte = carteMap.entrySet().stream()
                .min(Comparator.comparingInt(entry -> entry.getValue().anul()));

        celMaiVecCarte.ifPresent(entry -> {
            Carte carte = entry.getValue();
            System.out.println("Titlu: " + carte.titlul());
            System.out.println("Autor: " + carte.autorul());
            System.out.println("Anul: " + carte.anul());
        });
    }


    private static Set<Carte> extragereAutor(Map<Integer, Carte> carteMap) {
        Set<Carte> cartiYual = carteMap.values().stream().filter((a)->a.autorul().equals("Yuval Noah Harari")).collect(Collectors.toSet());
        cartiYual.forEach(System.out::println);
        return  cartiYual;
    }

    private static void adaugareCarte(Map<Integer, Carte> carteMap) {
        Scanner sc=new Scanner(System.in);
        System.out.println("titlul cartii?");
        String titlu=sc.nextLine();
        System.out.println("autorul cartii?");
        String autor=sc.nextLine();
        System.out.println("anul?");
        int an=sc.nextInt();
        int max=0;
        for (Map.Entry<Integer, Carte> entry : carteMap.entrySet()) {
            if(max<entry.getKey()){
                max= entry.getKey();
            }
        }
        //carteMap.putIfAbsent(carteMap.size()+1,new Carte(titlu,autor,an));
        carteMap.putIfAbsent(max+1,new Carte(titlu,autor,an));
    }

    private static void stergereCarte(Map<Integer, Carte> carteMap) {
        Scanner sc=new Scanner(System.in);
        System.out.println("ce carte doriti sa stergeti?");
        String opt=sc.nextLine();
        for (Map.Entry<Integer, Carte> entry : carteMap.entrySet()) {
            if(entry.getValue().titlul().equals(opt)){
                carteMap.remove(entry.getKey());
                return;
            }
        }
    }
}
